package com.uniwear.backend.controller;

import com.uniwear.backend.dto.OrderDto;
import com.uniwear.backend.dto.Response;
import com.uniwear.backend.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    @ResponseBody
    public Response getOrderList(@RequestAttribute Long memberId) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("주문 리스트 정보입니다.");
        response.setData(orderService.getOrderList(memberId));

        return response;
    }

    @GetMapping("/count")
    @ResponseBody
    public Response getOrderListCount(@RequestAttribute Long memberId) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("주문 리스트 정보입니다.");
        response.setData(orderService.getOrderListCount(memberId));

        return response;
    }

    @GetMapping("/{orderNumber}")
    @ResponseBody
    public Response getOrderDetail(@PathVariable String orderNumber) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("주문 상세 정보입니다.");
        response.setData(orderService.getOrderDetail(orderNumber));

        return response;
    }

    /**
     * 1. 토큰 발급
     * 2. 데이터 검증
     * 3. 재고 확인 및 주문 등록
     * 4. 결제 승인
     */
    @PostMapping("")
    @ResponseBody
    public Response insertOrder(@RequestBody OrderDto.Req orderReq) {
        Response response = new Response();
        WebClient bootPayApiClient = WebClient.builder().baseUrl("https://api.bootpay.co.kr/").build();

        // 1. 토큰 발급
        String accessToken = orderService.getAccessToken(bootPayApiClient);

        // 2. 데이터 검증
        boolean flag = orderService.verifyData(bootPayApiClient, accessToken, orderReq);
        if (!flag) {
            response.setCode("F");
            response.setMessage("주문 데이터가 잘못되어있습니다.");
            return response;
        }

        // 3. 재고 확인 및 주문 등록
        // 아직 재고관리 안하므로 재고 확인 로직이 현재 들어가있지는 않음
        flag = orderService.insertOrder(orderReq, response);
        if (!flag) {
            response.setCode("F");
            response.setMessage("주문 저장 중 문제가 발생하였습니다.");
            return response;
        }

        // 4. 결제 승인
        flag = orderService.approvePayment(bootPayApiClient, accessToken, orderReq.getReceiptId());
        if (!flag) {
            response.setCode("F");
            response.setMessage("결제 승인 중 문제가 발생하였습니다.");
            return response;
        }
        
        response.setCode("S");
        response.setMessage("주문을 성공적으로 저장하였습니다.");
        return response;
    }
}