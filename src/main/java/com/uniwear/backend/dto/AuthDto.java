package com.uniwear.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Res {
        private MemberDto.Res.Auth user;
        private String token;

        @Builder
        public Res (MemberDto.Res.Auth user, String token) {
            this.user = user;
            this.token = token;
        }
    }
}