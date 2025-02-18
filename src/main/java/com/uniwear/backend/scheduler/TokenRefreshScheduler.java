package com.uniwear.backend.scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.uniwear.backend.dto.GovernmentDto;
import com.uniwear.backend.dto.InstagramDto;
import com.uniwear.backend.exception.ApiCallFailedException;
import com.uniwear.backend.util.MultiValueMapConverter;
import com.uniwear.backend.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

// Open API 사용을 위한 토큰 갱신 스케줄러
// 각각의 API 제공업체마다 토큰 만료기한이 정해져있음
//
// 인스타그램 : 60일마다 갱신
@Slf4j
@Component
public class TokenRefreshScheduler {
    

    @Autowired
    private MultiValueMapConverter multiValueMapConverter;

    @Autowired
    private RedisUtil redisUtil;
    
    // 인스타그램 토큰 갱신 스케줄
    // 주기 : 59일 마다
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 59)
    public void refreshTokenSchedule() {
        WebClient webClient = WebClient.builder().baseUrl("https://graph.instagram.com/").build();
        String instaToken = redisUtil.getData("instagram", "token");
        
        InstagramDto.GetLongToken.Req instagramGetLongTokenReq = InstagramDto.GetLongToken.Req.builder().grantType("ig_refresh_token").accessToken(instaToken).build();

        MultiValueMap<String, String> queryParams = multiValueMapConverter.convert(instagramGetLongTokenReq);
        
        webClient.get()
            .uri(uriBuilder -> 
                uriBuilder
                    .path("/refresh_access_token")
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
            .subscribe(result -> {
                log.info("인스타그램 토큰이 정상 갱신 되었습니다. " + result.toString());
                redisUtil.setDataExpire("token", "instagram", result.getAccessToken(), result.getExpiresIn() * 1000L);
            });
    }

    // 법정공휴일 데이터 연동 스케줄
    // 주기 : 59일 마다
    // 임시 테스트용 설정값이므로 1년주기로 실행되도록 변경해야 함
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 59)
    public void getRestDeInfo() {
        ObjectMapper mapper =  new XmlMapper();
        String year = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy"));

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://apis.data.go.kr/");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        WebClient webClient = WebClient.builder().uriBuilderFactory(factory).baseUrl("http://apis.data.go.kr/").build();
        
        // API 호출 파라미터 준비
        GovernmentDto.Req governmentReq = GovernmentDto.Req.builder().serviceKey(redisUtil.getData("goverment", "serviceKey")).solYear(year).numOfRows(365).build();
        MultiValueMap<String, String> multimap = multiValueMapConverter.convert(governmentReq);

        // API 호출
        webClient.get()
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
            .subscribe(str -> {
                GovernmentDto.Res govermentRes = null;
                GovernmentDto.Error.Res govermentErrorRes = null;
                try {
                    govermentRes = mapper.readValue(str, GovernmentDto.Res.class);
                } catch (JsonMappingException e) {
                    try {
                        govermentErrorRes = mapper.readValue(str, GovernmentDto.Error.Res.class);
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

                log.info("공휴일 데이터를 성공적으로 연동 및 저장하였습니다." + date);
            });
    }
}
