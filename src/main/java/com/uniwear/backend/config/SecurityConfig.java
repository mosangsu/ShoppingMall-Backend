package com.uniwear.backend.config;

import com.uniwear.backend.filter.JwtRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/hello").permitAll()
            .antMatchers("/hello2").hasRole("NOT_PERMITTED")
            .antMatchers("/order/**").authenticated()
            .antMatchers("/cart/**").authenticated()
            .antMatchers("/user/login").permitAll()             // 로그인
            .antMatchers("/user/token/refresh").permitAll()     // 액세스 토큰 갱신
            .antMatchers("/user/**").authenticated()
            .antMatchers("/**").permitAll()
            // .antMatchers("/api/admin").hasRole("ADMIN")
            .and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}