package com.uniwear.backend.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class InstagramDto {

    public static class GetLongToken {
        @Getter
        @Setter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Req {
            private String clientSecret;
            private String grantType;
            private String accessToken;

            @Builder
            public Req (String clientSecret, String grantType, String accessToken) {
                this.clientSecret = clientSecret;
                this.grantType = grantType;
                this.accessToken = accessToken;
            }
        }
        @Getter
        @Setter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Res {
            private String accessToken;
            private String tokenType;
            private int expiresIn;
        
            @Builder
            public Res (String accessToken, String tokenType, int expiresIn) {
                this.accessToken = accessToken;
                this.tokenType = tokenType;
                this.expiresIn = expiresIn;
            }
        
            @Override
            public String toString() {
                return "{ accessToken : " + accessToken + ", " + "tokenType : " + tokenType + ", " + "expiresIn : " + expiresIn + " }";
            }
        }
    }
    public static class GetShortToken {
        @Getter
        @Setter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Req {
            private String clientId;
            private String clientSecret;
            private String grantType;
            private String redirectUri;
            private String code;

            @Builder
            public Req (String clientId, String clientSecret, String grantType, String redirectUri, String code) {
                this.clientId = clientId;
                this.clientSecret = clientSecret;
                this.grantType = grantType;
                this.redirectUri = redirectUri;
                this.code = code;
            }
        }
        @Getter
        @Setter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Res {
            private String accessToken;
            private Long userId;
        
            @Builder
            public Res (String accessToken, Long userId) {
                this.accessToken = accessToken;
                this.userId = userId;
            }
        
            @Override
            public String toString() {
                return "{ accessToken : " + accessToken + ", " + "userId : " + userId + " }";
            }
        }
    }
    public static class Error {
        @Getter
        @Setter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Res {

            private String errorType;
            private int code;
            private String errorMessage;
        
            @Builder
            public Res (String errorType, int code, String errorMessage) {
                this.errorType = errorType;
                this.code = code;
                this.errorMessage = errorMessage;
            }
        
            @Override
            public String toString() {
                return "{ errorType : " + errorType + ", " + "code : " + code + ", " + "errorMessage : " + errorMessage + " }";
            }
        }
    }
}