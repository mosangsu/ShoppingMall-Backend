package com.uniwear.backend.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.uniwear.backend.dto.EstimateDto;
import com.uniwear.backend.dto.Response;
import com.uniwear.backend.service.EstimateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/estimate")
public class EstimateController {

    @Autowired
    private EstimateService estimateService;

    @PostMapping("")
    @ResponseBody
    public Response insertEstimate(@RequestAttribute(required = false) Long memberId, @RequestPart(value="files", required = false) List<MultipartFile> files, @RequestPart(value="data", required = false) EstimateDto.Req data) {
        Response response = new Response();
        boolean flag = estimateService.insertEstimate(memberId, files, data, response);
        if (flag) {
            response.setCode("S");
            response.setMessage("견적문의 글이 성공적으로 저장되었습니다.");
        } else {
            response.setCode("F");
            response.setMessage("견적문의 글이 저장되지 않았습니다.");
        }

        return response;
    }

    @PostMapping("/{estimateId}")
    @ResponseBody
    public Response updateEstimate(@RequestAttribute(required = false) Long memberId, @RequestPart(value="files", required = false) List<MultipartFile> files, @RequestPart(value="data", required = false) EstimateDto.Req data, @PathVariable Long estimateId) {
        Response response = new Response();
        boolean flag = estimateService.updateEstimate(memberId, estimateId, files, data, response);
        if (flag) {
            response.setCode("S");
            response.setMessage("견적문의 글이 성공적으로 저장되었습니다.");
        } else {
            response.setCode("F");
            response.setMessage("견적문의 글이 저장되지 않았습니다.");
        }

        return response;
    }

    @GetMapping("")
    @ResponseBody
    public Response getEstimateList(HttpServletRequest request, @RequestAttribute Long memberId) {
        Response response = new Response();
        Object result = estimateService.getEstimateList(memberId);

        response.setCode("S");
        response.setMessage("견적문의 리스트 정보입니다.");
        response.setData(result);

        return response;
    }

    @GetMapping("/count")
    @ResponseBody
    public Response getEstimateListCount(@RequestAttribute Long memberId) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("견적문의 리스트 카운트 정보입니다.");
        response.setData(estimateService.getEstimateListCount(memberId));

        return response;
    }

    @GetMapping("/{estimateId}")
    @ResponseBody
    public Response getProductDetail(@PathVariable Long estimateId) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("견적문의 상세 정보입니다.");
        response.setData(estimateService.getEstimateDetail(estimateId));

        return response;
    }
}