package com.uniwear.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import com.uniwear.backend.dto.BootPayDto;
import com.uniwear.backend.dto.OrderDto;
import com.uniwear.backend.dto.Response;
import com.uniwear.backend.entity.Cart;
import com.uniwear.backend.entity.CartProductGroup;
import com.uniwear.backend.entity.CartProductOption;
import com.uniwear.backend.entity.MemberCoupon;
import com.uniwear.backend.entity.Order;
import com.uniwear.backend.entity.OrderProduct;
import com.uniwear.backend.entity.OrderProductGroup;
import com.uniwear.backend.entity.OrderProductOption;
import com.uniwear.backend.entity.member.Member;
import com.uniwear.backend.enums.OrderStatus;
import com.uniwear.backend.repository.CartRepository;
import com.uniwear.backend.repository.MemberCouponRepository;
import com.uniwear.backend.repository.MemberRepository;
import com.uniwear.backend.repository.OrderRepository;
import com.uniwear.backend.repository.jpql.JOrderRepository;
import com.uniwear.backend.util.RedisUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class OrderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private JOrderRepository jOrderRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MemberCouponRepository memberCouponRepository;

    public List<OrderDto.Res> getOrderList(Long memberId) {
        return orderRepository.findAllByMemberMemberId(memberId).stream().map(p -> modelMapper.map(p, OrderDto.Res.class))
                .collect(Collectors.toList());
    }

    public List<OrderDto.Count.Res> getOrderListCount(Long memberId) {
        List<OrderDto.Count.Res> result = jOrderRepository.countOrderGroupByStatus(memberId);
        return result;
    }

    public OrderDto.Res getOrderDetail(String orderNumber) {
        return modelMapper.map(orderRepository.findByOrderNumber(orderNumber), OrderDto.Res.class);
    }

    public String getAccessToken(WebClient bootPayApiClient) {

        BootPayDto.GetToken.Req bootPayGetTokenReq = BootPayDto.GetToken.Req.builder().applicationId("623939b32701800023f68294").privateKey("J1Qcl0GavDaisTdxbyD6zgwER71mlMYwuDG4fcuKRAE=").build();

        // 1. BootPay 토큰 발급
        BootPayDto.GetToken.Res token = bootPayApiClient.post()
            .uri("/request/token")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                BodyInserters.fromValue(bootPayGetTokenReq)
            )
            .retrieve()
            .onStatus(HttpStatus::isError, res -> {
                log.error("Response status: {}", res.statusCode());
                res.bodyToMono(String.class).subscribe(result -> {log.error("Response body: {}", result.toString());});
                
                return res.createException();
            })
            .bodyToMono(BootPayDto.GetToken.Res.class)
            .onErrorResume(error -> {
                log.error(error.getMessage());
                return Mono.empty();
            })
            .block();

        return token.getData().getToken();
    }

    public boolean verifyData(WebClient bootPayApiClient, String accessToken, OrderDto.Req request) {
        MemberCoupon memberCoupon = null;
        Member member = null;

        // 2. BootPay 결제 내용 조회
        BootPayDto.Receipt.Res receipt = bootPayApiClient.get()
            .uri(uriBuilder -> 
                uriBuilder
                    .path("/receipt/{id}")
                    .build(request.getReceiptId())
            )
            .header("Authorization", accessToken)
            .retrieve()
            .onStatus(HttpStatus::isError, res -> {
                log.error("Response status: {}", res.statusCode());
                res.bodyToMono(String.class).subscribe(result -> {log.error("Response body: {}", result.toString());});
                
                return res.createException();
            })
            .bodyToMono(BootPayDto.Receipt.Res.class)
            .onErrorResume(error -> {
                log.error(error.getMessage());
                return Mono.empty();
            })
            .block();
        if (!receipt.getData().getStatus().equals("2")) { return false; }   // 결제 승인 대기 상태가 아님

        // 3. 결제 금액 검증 (DB 데이터 기반 산정액 <-> 부트페이 결제 금액)
        int price = cartService.getCombinedPrice(request.getCartId());
        if (price != receipt.getData().getPrice()) { return false; }    // 금액 산정액과 결제 요청 금액이 다름

        // 4. 쿠폰 검증
        if (request.getCouponId() != null) {
            try {
                memberCoupon = memberCouponRepository.findByMemberMemberIdAndCouponCouponId(request.getMemberId(), request.getCouponId());
                if (memberCoupon.getCoupon().getComparisonAmount() > receipt.getData().getPrice()) return false;    // 결제금액이 쿠폰 사용가능 기준치를 만족하지 못함
            } catch (NoSuchElementException e) {    // 아무것도 조회되지 않았을 경우
                log.error(e.getMessage());
                return false;   // 존재하지 않는 쿠폰
            }
        }
        
        // 5. 적립금 검증
        if (request.getPoint() > 0) {
            member = memberRepository.findById(request.getMemberId()).get();

            if (member.getPoint() < request.getPoint()) return false;   // 보유포인트보다 사용하려는 포인트가 더 많을 경우
        }

        return true;
    }

    public boolean insertOrder(OrderDto.Req request, Response response) {

        Order order = modelMapper.map(request, Order.class);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PAYMENT_COMPLETE);
        
        Member member = memberRepository.findById(request.getMemberId()).get();
        order.setMember(member);

        // // 중복 데이터 체크
        // // 기존에는 save할 때 JPA가 알아서 체크하지만 사용자ID, 상품ID, 인쇄타입N 에 대한 중복체크를 해야해서 직접 구현
        List<Cart> cartList = cartRepository.findAllById(request.getCartId());

        for (Cart cart : cartList) {
            OrderProduct orderProduct = new OrderProduct(cart);
            order.addOrderProduct(orderProduct);
            for (CartProductGroup cpg : cart.getCartProductGroup()) {
                OrderProductGroup orderProductGroup = new OrderProductGroup(cpg);
                orderProduct.addOrderProductGroup(orderProductGroup);
                for (CartProductOption cpo : cpg.getCartProductOption()) {
                    OrderProductOption orderProductOption = new OrderProductOption(cpo);
                    orderProductGroup.addOrderProductOption(orderProductOption);
                }
            }
        }
        orderRepository.save(order);
        response.setData(order.getOrderNumber());

        return true;
    }
    
    public boolean approvePayment(WebClient bootPayApiClient, String accessToken, String receiptId) {

        BootPayDto.Submit.Req bootPaySubmitReq = BootPayDto.Submit.Req.builder().receiptId(receiptId).build();

        BootPayDto.Receipt.Res receipt = bootPayApiClient.post()
            .uri("/submit.json")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", accessToken)
            .body(
                BodyInserters.fromValue(bootPaySubmitReq)
            )
            .retrieve()
            .onStatus(HttpStatus::isError, res -> {
                log.error("Response status: {}", res.statusCode());
                res.bodyToMono(String.class).subscribe(result -> {log.error("Response body: {}", result.toString());});
                
                return res.createException();
            })
            .bodyToMono(BootPayDto.Receipt.Res.class)
            .onErrorResume(error -> {
                log.error(error.getMessage());
                return Mono.empty();
            })
            .block();
        if (receipt.getStatus() == 200)
            return true;
        else return false;
    }

    // 주문번호 생성하여 리턴하는 메소드
    // Redis로 시퀀스를 관리하고 한번 생성할 때마다 시퀀스값이 1씩 올라가도록 구현
    // 올라간 시퀀스 값은 하루주기로 초기화
    public String generateOrderNumber () {
        String result = "";
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String sequenceString = redisUtil.getData("orderNumber", "sequence");
        Integer sequence = null;
        try {
            sequence = Integer.valueOf(sequenceString);
        } catch (NumberFormatException e) {
            log.warn(e.getMessage());
        }
        
        if (sequence == null) {
            sequence = 1;
            redisUtil.setData("orderNumber", "sequence", sequence.toString());
        } else {
            sequence++;
        }
        
        String yyyyMMdd = now.format(formatter);
        result = yyyyMMdd + String.format("%08d", sequence);

        redisUtil.setData("orderNumber", "sequence", sequence.toString());

        return result;
    }
}