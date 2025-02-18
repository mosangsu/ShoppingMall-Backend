package com.uniwear.backend.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BootPayDto {

    public static class GetToken {
        @Getter
        @Setter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Req {
            private String applicationId;
            private String privateKey;

            @Builder
            public Req (String applicationId, String privateKey) {
                this.applicationId = applicationId;
                this.privateKey = privateKey;
            }
        }
        @Getter
        @Setter
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Res {
            private int status;
            private int code;
            private String message;
            private Data data;
        
            @Override
            public String toString() {
                return "{ status : " + status + ", " + "code : " + code + ", " + "message : " + message + ", " + "token : " + data.token + ", " + "serverTime : " + data.serverTime + ", " + "expiredAt : " + data.expiredAt + " }";
            }

            @Getter
            @Setter
            public static class Data {
                private String token;
                private int serverTime;
                private int expiredAt;
            }
        }
    }
    public static class Receipt {
        @Getter
        @Setter
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Res {
            private int status;
            private int code;
            private String message;
            private Data data;
        
            @Override
            public String toString() {
                return "{ receiptId : " + data.receiptId + ", " + "name : " + data.name + " }";
            }

            @Getter
            @Setter
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class Data {
                private String receiptId;
                private String orderId;
                private String name;
                private String itemName;
                private int price;
                private int taxFree;
                private int remainPrice;
                private int remainTaxFree;
                private int cancelledPrice;
                private int cancelledTaxFree;
                private String receiptUrl;
                private String unit;
                private String pg;
                private String method;
                private String pgName;
                private String methodName;
                private String status;
                private String statusEn;
                private String statusKo;
                @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
                private Date requestedAt;
                @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
                private Date purchasedAt;
                private PaymentData paymentData;
            }

            @Getter
            @Setter
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
            public static class PaymentData {
                private String receiptId;
                private String n;
                private int p;
                private String tid;
                private String pg;
                private String pm;
                private String pgA;
                private String pmA;
                private String oId;
                @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
                private Date pAt;
                @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
                private Date rAt;
                private int s;
                private int g;
            }
        }
    }
    public static class Submit {
        @Getter
        @Setter
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Req {
            private String receiptId;

            @Builder
            public Req (String receiptId) {
                this.receiptId = receiptId;
            }
        }
    }
}