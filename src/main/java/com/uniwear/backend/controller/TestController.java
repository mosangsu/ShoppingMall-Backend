package com.uniwear.backend.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.uniwear.backend.dto.GovernmentDto;
import com.uniwear.backend.dto.Response;
import com.uniwear.backend.exception.ApiCallFailedException;
import com.uniwear.backend.util.MultiValueMapConverter;
import com.uniwear.backend.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MultiValueMapConverter multiValueMapConverter;

    @PutMapping("/1")
    @ResponseBody
    public Response hello1(HttpServletRequest request, @RequestBody List<Long> ids) {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("공휴일 데이터를 성공적으로 연동 및 저장하였습니다.");
        return response;
    }

    @GetMapping("/2")
    @ResponseBody
    public Response hello2(HttpServletRequest request) {

        String year= "2022";
        GovernmentDto.Res govermentRes = null;
        GovernmentDto.Error.Res govermentErrorRes = null;
        Response response = new Response();
        ObjectMapper mapper =  new XmlMapper();

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://apis.data.go.kr/");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        WebClient webClient = WebClient.builder().uriBuilderFactory(factory).baseUrl("http://apis.data.go.kr/").build();
        
        // API 호출 파라미터 준비
        GovernmentDto.Req governmentReq = GovernmentDto.Req.builder().serviceKey("L3WuhbHLOQlUrRwUL%2Bwo1DRfZXspHGB1B3dOBQ3PcuIjSAdKSjHmdu%2BXGHBijE7OMkKoMl2OWkCsCu0hjA5jnA%3D%3D").solYear(year).numOfRows(365).build();
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