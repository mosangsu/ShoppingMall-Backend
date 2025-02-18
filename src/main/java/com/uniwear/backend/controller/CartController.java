package com.uniwear.backend.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.uniwear.backend.dto.CartDto;
import com.uniwear.backend.dto.Response;
import com.uniwear.backend.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("")
    @ResponseBody
    public Response getCartList(HttpServletRequest request, @RequestAttribute Long memberId, @RequestParam(required = false) List<Long> ids) {
        Response response = new Response();
        Object result = null;

        if(ids != null && ids.size() > 0)
            result = cartService.getCartList(memberId, ids);
        else
            result = cartService.getCartList(memberId);

        response.setCode("S");
        response.setMessage("장바구니 리스트 정보입니다.");
        response.setData(result);

        return response;
    }

    @GetMapping("/count")
    @ResponseBody
    public Response getCartListCount(@RequestAttribute Long memberId) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("장바구니 리스트 정보입니다.");
        response.setData(cartService.getCartListCount(memberId));

        return response;
    }

    @PostMapping("")
    @ResponseBody
    public Response insertCart(@RequestBody CartDto.Req cartReq) {
        Response response = new Response();
        boolean flag = cartService.insertCart(cartReq);
        if (flag) {
            response.setCode("S");
            response.setMessage("장바구니를 성공적으로 저장하였습니다.");
        } else {
            response.setCode("F");
            response.setMessage("장바구니에 저장되지 않았습니다.");
        }

        return response;
    }

    @DeleteMapping("")
    @ResponseBody
    public Response deleteCarts(@RequestBody List<Long> ids) {
        Response response = new Response();
        boolean flag = cartService.deleteCarts(ids);
        if (flag) {
            response.setCode("S");
            response.setMessage("장바구니를 성공적으로 저장하였습니다.");
        } else {
            response.setCode("F");
            response.setMessage("장바구니에 저장되지 않았습니다.");
        }

        return response;
    }
}