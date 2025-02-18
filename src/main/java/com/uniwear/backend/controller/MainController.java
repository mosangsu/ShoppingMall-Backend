package com.uniwear.backend.controller;

import java.util.HashMap;
import java.util.Map;

import com.uniwear.backend.dto.Response;
import com.uniwear.backend.service.MainService;
import com.uniwear.backend.service.UserPrincipalDetailService;
import com.uniwear.backend.util.CookieUtil;
import com.uniwear.backend.util.JwtUtil;
import com.uniwear.backend.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MainService mainService;

    @Autowired
    UserPrincipalDetailService userPrincipalDetailService;    

    @GetMapping("/instaToken")
    @ResponseBody
    public Response instaToken() {
        Response response = new Response();
        Map<String, String> map = new HashMap<>();
        map.put("token", redisUtil.getData("instagram", "token"));
        map.put("userId", redisUtil.getData("instagram", "userId"));

        response.setCode("S");
        response.setMessage("인스타피드를 가져오기 위한 토큰 정보입니다.");
        response.setData(map);

        return response;
    }

    @GetMapping("/menu")
    @ResponseBody
    public Response menu() {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("상단 고정 메뉴 정보입니다.");
        response.setData(mainService.getMenuList());
        return response;
    }

    @GetMapping("/holiday")
    @ResponseBody
    public Response holiday(@RequestParam(required = false) String year) {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("공휴일 정보입니다.");
        response.setData(mainService.getHolidays(year));
        return response;
    }

    @GetMapping("/membership")
    @ResponseBody
    public Response membership() {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("멤버쉽 정보입니다.");
        response.setData(mainService.getMembership());
        return response;
    }

    @GetMapping("/brand")
    @ResponseBody
    public Response brand() {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("브랜드 정보입니다.");
        response.setData(mainService.getSuppliers());
        return response;
    }

    @GetMapping("/category/root")
    @ResponseBody
    public Response rootCategory() {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("루트 카테고리 정보입니다.");
        response.setData(mainService.getRootCategories());
        return response;
    }

    @GetMapping("/category/{categoryId}")
    @ResponseBody
    public Response category(@PathVariable Long categoryId) {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("카테고리 정보입니다.");
        response.setData(mainService.getCategory(categoryId));
        return response;
    }

    @GetMapping("/printType")
    @ResponseBody
    public Response printType() {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("인쇄방식 정보입니다.");
        response.setData(mainService.getPrintTypeList());
        return response;
    }

    @GetMapping("/printType/{printTypeId}/color")
    @ResponseBody
    public Response colorByPrintType(@PathVariable Long printTypeId) {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("인쇄색상 정보입니다.");
        response.setData(mainService.getColorsByPrintType(printTypeId));
        return response;
    }

    @GetMapping("/printType/{printTypeId}/size")
    @ResponseBody
    public Response sizeByPrintType(@PathVariable Long printTypeId) {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("인쇄사이즈 정보입니다.");
        response.setData(mainService.getSizesByPrintType(printTypeId));
        return response;
    }

    @GetMapping("/caseType")
    @ResponseBody
    public Response caseType() {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("납품사례 유형 정보입니다.");
        response.setData(mainService.getCaseTypeList());
        return response;
    }

    @GetMapping("/printPosition")
    @ResponseBody
    public Response printPosition() {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("인쇄 위치 정보입니다.");
        response.setData(mainService.getPrintPositionList());
        return response;
    }
}