package com.uniwear.backend.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 정부기관 API 연동 DTO
public class GovernmentDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Req {
        private String serviceKey;
        private String solYear;
        private int numOfRows;

        @Builder
        public Req (String serviceKey, String solYear, int numOfRows) {
            this.serviceKey = serviceKey;
            this.solYear = solYear;
            this.numOfRows = numOfRows;
        }
    }
    
    @Getter
    @Setter
    public static class Res {
        private Header header;
        private Body body;
    
        @Getter
        @Setter
        public static class Header {
            private String resultCode;
            private String resultMsg;
        }
        
        @Getter
        @Setter
        public static class Body {
            private int numOfRows;
            private int pageNo;
            private int totalCount;
            private List<Item> items;
        }
    
        @Getter
        @Setter
        public static class Item {
            private String dateKind;
            private String dateName;
            private String isHoliday;
            private String locdate;
            private int seq;
        }
    }
    
    public static class Error {
        @Getter
        @Setter
        public static class Res {

            private CmmMsgHeader cmmMsgHeader;
        
            @Override
            public String toString() {
                return "{ errMsg : " + cmmMsgHeader.errMsg + ", " + "returnAuthMsg : " + cmmMsgHeader.returnAuthMsg + ", " + "returnReasonCode : " + cmmMsgHeader.returnReasonCode + " }";
            }
        }
        @Getter
        @Setter
        public static class CmmMsgHeader {
            private String errMsg;
            private String returnAuthMsg;
            private int returnReasonCode;
        }
    }
}