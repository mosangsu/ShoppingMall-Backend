package com.uniwear.backend.controller;

import java.util.List;

import com.uniwear.backend.dto.Response;
import com.uniwear.backend.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    @ResponseBody
    public Response getProductList(@RequestParam(required = false) Long categoryId, @RequestParam(required = false) List<String> tags,
    @RequestParam(required = false) Integer goe, @RequestParam(required = false) Integer loe, @RequestParam(required = false) String order, Pageable pageable) {
        Response response = new Response();
        Object result = productService.getProductList(categoryId, tags, goe, loe, order, pageable);

        response.setCode("S");
        response.setMessage("상품 리스트 정보입니다.");
        response.setData(result);

        return response;
    }

    @GetMapping("/{productId}")
    @ResponseBody
    public Response getProductDetail(@PathVariable Long productId) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("상품 상세 정보입니다.");
        response.setData(productService.getProductDetail(productId));

        return response;
    }

    @GetMapping("/{productId}/option")
    @ResponseBody
    public Response getProductOption(@PathVariable Long productId) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("상품 상세 정보입니다.");
        response.setData(productService.getProductOption(productId));

        return response;
    }
}