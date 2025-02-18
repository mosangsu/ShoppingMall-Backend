package com.uniwear.backend.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.uniwear.backend.dto.GovernmentDto;
import com.uniwear.backend.dto.InstagramDto;
import com.uniwear.backend.dto.Response;
import com.uniwear.backend.exception.ApiCallFailedException;
import com.uniwear.backend.exception.TokenIsNotIssuedException;
import com.uniwear.backend.util.MultiValueMapConverter;
import com.uniwear.backend.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    

    @Autowired
    private MultiValueMapConverter multiValueMapConverter;

    @Autowired
    private RedisUtil redisUtil;

    // 인스타그램 단기토큰 요청
    @GetMapping("/code")
    @ResponseBody
    public void getCode(HttpServletResponse response) {
        try {
            response.sendRedirect("https://api.instagram.com/oauth/authorize?client_id=290169206439222&redirect_uri=https://www.frankit.com/api/admin/token&scope=user_profile,user_media&response_type=code");
        } catch (IOException e) {
            log.error("인스타그램 리다이렉트 중 오류가 발생하였습니다.");
        }
    }

    // 발급받은 인스타그램 단기 토큰을 장기토큰으로 교환
    @GetMapping("/token")
    @ResponseBody
    public Response hello(HttpServletRequest request) {
        Response response = new Response();
        WebClient instagramApiClient = WebClient.builder().baseUrl("https://api.instagram.com/").build();
        WebClient instagramGraphClient = WebClient.builder().baseUrl("https://graph.instagram.com/").build();
        try {
            // 1. Instagram Oauth 인증을 통해 받은 코드값을 가져옴
            String code = String.valueOf(request.getParameter("code"));
            // code = code.substring(0, code.length() - 2);
            
            // 2. 전송에 필요한 데이터를 모델에 담음
            InstagramDto.GetShortToken.Req instagramGetShortTokenReq = InstagramDto.GetShortToken.Req.builder().clientId("290169206439222").clientSecret("331a02cc2d5c93c041e19a6d049a9558").grantType("authorization_code").redirectUri("https://www.frankit.com/api/admin/token").code(code).build();

            // 3. 파라미터 전송을 위해 모델을 MultiValueMap 형태로 변환
            MultiValueMap<String, String> formData = multiValueMapConverter.convert(instagramGetShortTokenReq);

            // 4. 단기 토큰 발급 API 호출. block() 처리하여 응답값을 인스턴스에 담음
            InstagramDto.GetShortToken.Res shortTermToken = instagramApiClient.post()
                .uri("/oauth/access_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                    BodyInserters.fromFormData(formData)
                )
                .retrieve()
                .onStatus(HttpStatus::isError, res -> {
                    log.error("Response status: {}", res.statusCode());
                    res.bodyToMono(InstagramDto.Error.Res.class).subscribe(result -> {log.error("Response body: {}", result.toString());});
                    
                    return res.createException();
                })
                .bodyToMono(InstagramDto.GetShortToken.Res.class)
                .onErrorResume(error -> {
                    log.error(error.getMessage());
                    return Mono.empty();
                })
                .block();

            if (shortTermToken == null) {
                throw new TokenIsNotIssuedException("F", "단기 토큰 발급 중 문제가 발생하였습니다.");
            }

            // 5. 발급 받은 토큰에 대한 ID 정보 저장
            redisUtil.setData("instagram", "userId", String.valueOf(shortTermToken.getUserId()));

            log.info("단기 토큰 정보가 정상 저장되었습니다. " + shortTermToken.toString());
            
            // 6. 전송에 필요한 데이터를 모델에 담음
            InstagramDto.GetLongToken.Req instagramGetLongTokenReq = InstagramDto.GetLongToken.Req.builder().clientSecret("331a02cc2d5c93c041e19a6d049a9558").grantType("ig_exchange_token").accessToken(shortTermToken.getAccessToken()).build();

            // 7. 파라미터 전송을 위해 모델을 MultiValueMap 형태로 변환
            MultiValueMap<String, String> queryParams = multiValueMapConverter.convert(instagramGetLongTokenReq);

            // 8. 장기 토큰 발급 API 호출. 서버에서 장기적으로 사용할 목적이므로 장기 토큰으로 전환 필요.
            // 단기 토큰 : 1시간 후 만료, 장기 토큰 : 60일 후 만료
            InstagramDto.GetLongToken.Res longTermToken = instagramGraphClient.get()
                .uri(uriBuilder -> 
                    uriBuilder
                        .path("/access_token")
                        .queryParams(queryParams)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatus::isError, res -> {
                    log.error("Response status: {}", res.statusCode());
                    res.bodyToMono(String.class).subscribe(result -> {log.error("Response body: {}", result);});
                    
                    return res.createException();
                })
                .bodyToMono(InstagramDto.GetLongToken.Res.class)
                .onErrorResume(error -> {
                    log.error(error.getMessage());
                    return Mono.empty();
                })
                .block();

            if (longTermToken == null) {
                throw new TokenIsNotIssuedException("F", "장기 토큰 발급 중 문제가 발생하였습니다.");
            }

            // 9. 발급된 장기 토큰을 Redis에 저장
            redisUtil.setDataExpire("instagram", "token", longTermToken.getAccessToken(), longTermToken.getExpiresIn() * 1000L);

            log.info("인스타그램 토큰이 정상 저장되었습니다. " + longTermToken.toString());
            
            response.setCode("S");
            response.setMessage("성공적으로 장기 토큰을 발급받았습니다!");
            response.setData(longTermToken);
        
        } catch (TokenIsNotIssuedException e) {
            response.setCode(e.getCode());
            response.setMessage(e.getMessage());
        }

        return response;
    }

    // 공공 데이터 API(data.go.kr)의 API 키 초기화
    // 2년마다 만료되므로 갱신
    @PostMapping("/govApiKey")
    @ResponseBody
    public Response setgovApiKey(@RequestBody GovernmentDto.Req request) {
        Response response = new Response();

        redisUtil.setData("goverment", "serviceKey", request.getServiceKey());
        
        response.setCode("S");
        response.setMessage("정부 공공 데이터 API 키를 성공적으로 저장하였습니다.");
        response.setData(redisUtil.getData("goverment", "serviceKey"));
        return response;
    }

    // 공휴일 정보를 가져오는 API
    // 가져온 정보는 Redis에 저장
    @GetMapping("/restDeInfo")
    @ResponseBody
    public Response getRestDeInfo(@RequestParam(required = false) String year) {
        GovernmentDto.Res govermentRes = null;
        GovernmentDto.Error.Res govermentErrorRes = null;
        Response response = new Response();
        ObjectMapper mapper =  new XmlMapper();
        if (year == null || year.equals("")) year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://apis.data.go.kr/");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        WebClient webClient = WebClient.builder().uriBuilderFactory(factory).baseUrl("http://apis.data.go.kr/").build();
        
        // API 호출 파라미터 준비
        GovernmentDto.Req governmentReq = GovernmentDto.Req.builder().serviceKey(redisUtil.getData("goverment", "serviceKey")).solYear(year).numOfRows(365).build();
        MultiValueMap<String, String> multimap = multiValueMapConverter.convert(governmentReq);

        // API 호출
        String str = webClient.get()
            .uri(uriBuilder -> 
                uriBuilder
                    .path("/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                    .queryParams(multimap)
                    .build()
            )
            .retrieve()
            .onStatus(HttpStatus::isError, res -> {
                log.error("Response status: {}", res.statusCode());
                res.bodyToMono(GovernmentDto.Error.Res.class).subscribe(result -> {log.error("Response body: {}", result.toString());});
                
                return res.createException();
            })
            .bodyToMono(String.class)
            .onErrorResume(error -> {
                log.error(error.getMessage());
                return Mono.empty();
            })
            .block();

        if (str == null) {
            throw new ApiCallFailedException("F", "정부 공공 데이터 API 호출 중 오류가 발생했습니다.", null);
        }

        try {
            govermentRes = mapper.readValue(str, GovernmentDto.Res.class);
        } catch (JsonMappingException e) {
            try {
                govermentErrorRes = mapper.readValue(str, GovernmentDto.Error.Res.class);
                response.setCode("F");
                response.setMessage("요청 값이 올바르지 않습니다.");
                response.setData(govermentErrorRes);
                throw new ApiCallFailedException("F", "요청 값이 올바르지 않습니다.", govermentErrorRes);
            } catch (JsonProcessingException e1) {
                log.error(e1.getMessage());
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        List<String> date = govermentRes.getBody().getItems().stream().map(item -> item.getLocdate()).collect(Collectors.toList());
        
        redisUtil.deleteData("holiday", "date:" + year);
        redisUtil.setDataList("holiday", "date:" + year, date);
        
        response.setCode("S");
        response.setMessage("공휴일 데이터를 성공적으로 연동 및 저장하였습니다.");
        response.setData(date);
        return response;
    }
}