package com.uniwear.backend.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uniwear.backend.dto.MemberDto;
import com.uniwear.backend.dto.Response;
import com.uniwear.backend.service.AuthService;
import com.uniwear.backend.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class MemberController {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public Response signUpUser(@RequestBody MemberDto.Req member){
        Response response = new Response();

        try{
            authService.signUpUser(member);
            response.setCode("S");
            response.setMessage("회원가입을 성공적으로 완료했습니다.");
        }
        catch(Exception e){
            response.setCode("failed");
            response.setMessage("회원가입을 하는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }

        return response;
    }

    @PostMapping("/login")
    public Response login(@RequestBody MemberDto.Req user, HttpServletResponse res) {
        Response response = new Response();

        try{
            response.setCode("S");
            response.setMessage("로그인을 성공적으로 완료했습니다.");
            response.setData(authService.loginUser(user, res));
        }
        catch(Exception e){
            response.setCode("failed");
            response.setMessage("로그인을 하는 도중 오류가 발생했습니다.");
            response.setData(e.getMessage());
        }

        return response;
    }

    @PostMapping("/token/refresh")
    @ResponseBody
    public Response refreshAccessToken(HttpServletRequest request) {
        Response response = new Response();
        response.setCode("S");
        response.setMessage("토큰을 성공적으로 갱신하였습니다.");
        response.setData(authService.refreshAccessToken(request));
        return response;
    }

    @GetMapping("/{id}")
    public Response getMember(@RequestAttribute Long memberId, @PathVariable Long id) {
        Response response = new Response();

        if (id != null && memberId == id) {
            response.setCode("S");
            response.setMessage("사용자 정보입니다.");
            response.setData(memberService.getMember(id));
        } else {
            response.setCode("F");
            response.setMessage("정보를 조회할 권한이 없습니다.");
        }

        return response;
    }

    // @PostMapping("/verify")
    // public Response verify(@RequestBody RequestVerifyEmail requestVerifyEmail, HttpServletRequest req, HttpServletResponse res) {
    //     Response response;
    //     try {
    //         Member member = authService.findByUsername(requestVerifyEmail.getUsername());
    //         authService.sendVerificationMail(member);
    //         response = new Response("S", "성공적으로 인증메일을 보냈습니다.", null);
    //     } catch (Exception exception) {
    //         response = new Response("F", "인증메일을 보내는데 문제가 발생했습니다.", exception);
    //     }
    //     return response;
    // }

    // @GetMapping("/verify/{key}")
    // public Response getVerify(@PathVariable String key) {
    //     Response response;
    //     try {
    //         authService.verifyEmail(key);
    //         response = new Response("S", "성공적으로 인증메일을 확인했습니다.", null);

    //     } catch (Exception e) {
    //         response = new Response("F", "인증메일을 확인하는데 실패했습니다.", null);
    //     }
    //     return response;
    // }

    // @GetMapping("/password/{key}")
    // public Response isPasswordUUIdValidate(@PathVariable String key) {
    //     Response response;
    //     try {
    //         if (authService.isPasswordUuidValidate(key))
    //             response = new Response("S", "정상적인 접근입니다.", null);
    //         else
    //             response = new Response("F", "유효하지 않은 Key값입니다.", null);
    //     } catch (Exception e) {
    //         response = new Response("F", "유효하지 않은 key값입니다.", null);
    //     }
    //     return response;
    // }

    // @PostMapping("/password")
    // public Response requestChangePassword(@RequestBody RequestChangePassword1 requestChangePassword1) {
    //     Response response;
    //     try {
    //         Member member = authService.findByUsername(requestChangePassword1.getUsername());
    //         if (!member.getEmail().equals(requestChangePassword1.getEmail())) throw new NoSuchFieldException("");
    //         authService.requestChangePassword(member);
    //         response = new Response("S", "성공적으로 사용자의 비밀번호 변경요청을 수행했습니다.", null);
    //     } catch (NoSuchFieldException e) {
    //         response = new Response("F", "사용자 정보를 조회할 수 없습니다.", null);
    //     } catch (Exception e) {
    //         response = new Response("F", "비밀번호 변경 요청을 할 수 없습니다.", null);
    //     }
    //     return response;
    // }

    // @PutMapping("/password")
    // public Response changePassword(@RequestBody RequestChangePassword2 requestChangePassword2) {
    //     Response response;
    //     try{
    //         Member member = authService.findByUsername(requestChangePassword2.getUsername());
    //         authService.changePassword(member,requestChangePassword2.getPassword());
    //         response = new Response("S","성공적으로 사용자의 비밀번호를 변경했습니다.",null);
    //     }catch(Exception e){
    //         response = new Response("F","사용자의 비밀번호를 변경할 수 없었습니다.",null);
    //     }
    //     return response;

    // }
}