package com.uniwear.backend.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import com.uniwear.backend.dto.AuthDto;
import com.uniwear.backend.dto.MemberDto;
import com.uniwear.backend.entity.member.Member;
import com.uniwear.backend.repository.MemberRepository;
import com.uniwear.backend.util.CookieUtil;
import com.uniwear.backend.util.JwtUtil;
import com.uniwear.backend.util.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private RedisUtil redisUtil;

    public void signUpUser(MemberDto.Req request) {
        Member newMember = new Member(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        memberRepository.save(newMember);
    }

    public AuthDto.Res loginUser(MemberDto.Req request, HttpServletResponse response) throws Exception{
        Member member = memberRepository.findByUsername(request.getUsername());
        if (member == null) throw new Exception ("아이디 또는 비밀번호가 일치하지 않습니다.");
        
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword()))
            throw new Exception ("아이디 또는 비밀번호가 일치하지 않습니다.");
            
        final String token = jwtUtil.generateToken(member);
        final String refreshJwt = jwtUtil.generateRefreshToken(member);
        Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt, (int) JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);
        redisUtil.setDataExpire("refreshToken", refreshJwt, member.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_MILLISECOND);
        response.addCookie(refreshToken);

        return AuthDto.Res.builder().user(MemberDto.Res.Auth.builder().id((long) member.getMemberId()).name(member.getUsername()).level(member.getMembership().getLevel()).build()).token(token).build();
    }

    public Object refreshAccessToken(HttpServletRequest request) {
        String token = null;
        Member member = null;

        Cookie refreshToken = cookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN_NAME);
        if (refreshToken == null) throw new BadCredentialsException("리프레시 토큰이 존재하지 않습니다.");

        String refreshJwt = refreshToken.getValue();
        String username = redisUtil.getData(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

        if(username.equals(jwtUtil.getUsername(refreshJwt))){
            member = memberRepository.findByUsername(username);
            token = jwtUtil.generateToken(member);
        }

        return AuthDto.Res.builder().user(MemberDto.Res.Auth.builder().id((long) member.getMemberId()).name(member.getUsername()).level(member.getMembership().getLevel()).build()).token(token).build();
    }
}