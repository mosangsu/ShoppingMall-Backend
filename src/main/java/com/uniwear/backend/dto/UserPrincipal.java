package com.uniwear.backend.dto;

import com.uniwear.backend.entity.member.Member;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {
    public UserPrincipal(Member member){
        super(member.getUsername(), member.getPassword(), AuthorityUtils.createAuthorityList(member.getRole().toString()));
    }
}