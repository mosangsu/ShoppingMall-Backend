package com.uniwear.backend.filter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uniwear.backend.service.UserPrincipalDetailService;
import com.uniwear.backend.util.CookieUtil;
import com.uniwear.backend.util.JwtUtil;
import com.uniwear.backend.util.RedisUtil;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserPrincipalDetailService userPrincipalDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader != null ? (authHeader.substring("Bearer ".length())) : "";

        String username = null;

        try{
            if(!accessToken.equals("")){
                username = jwtUtil.getUsername(accessToken);
            }
            if(username != null){
                UserDetails userDetails = userPrincipalDetailService.loadUserByUsername(username);

                if(jwtUtil.validateToken(accessToken,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
                request.setAttribute("username", username);
                request.setAttribute("memberId", jwtUtil.getMemberId(accessToken));
            }
        }catch (ExpiredJwtException e){
            log.info("Access 토큰 만료");
            response.setStatus(401);
            return;
        }catch(Exception e){
            log.error(e.getMessage());
        }

        filterChain.doFilter(request,response);
    }
}