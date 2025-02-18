package com.uniwear.backend.controller;

import com.uniwear.backend.dto.Response;
import com.uniwear.backend.service.DeliveryCaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/case")
public class DeliveryCaseController {

    @Autowired
    private DeliveryCaseService deliveryCaseService;

    @GetMapping("")
    @ResponseBody
    public Response getDeliveryCaseList(@RequestParam(required = false) Long printTypeId, @RequestParam(required = false) String caseType, Pageable pageable) {
        Response response = new Response();
        Object result = null;
        
        result = deliveryCaseService.getDeliveryCaseList(printTypeId, caseType, pageable);

        response.setCode("S");
        response.setMessage("납품사례 리스트 정보입니다.");
        response.setData(result);

        return response;
    }

    @GetMapping("/{deliveryCaseId}")
    @ResponseBody
    public Response getProductDetail(@PathVariable Long deliveryCaseId) {
        Response response = new Response();

        response.setCode("S");
        response.setMessage("납품사례 상세 정보입니다.");
        response.setData(deliveryCaseService.getDeliveryCaseDetail(deliveryCaseId));

        return response;
    }
}
