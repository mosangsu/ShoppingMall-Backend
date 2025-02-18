package com.uniwear.backend.dto;

import java.util.Date;
import java.util.List;

import com.uniwear.backend.enums.MemberType;
import com.uniwear.backend.enums.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberDto {
    @Getter
    @Setter
    public static class Req {
        private String username;
        private String password;
    }
    public static class Res {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Auth {
            private Long id;
            private String name;
            private int level;
            @Builder
            public Auth(Long id, String name, int level) {
                this.id = id;
                this.name = name;
                this.level = level;
            }
        }
        @Getter
        @Setter
        public static class Summary {
            private String username;
            private List<MemberCoupon> memberCoupon;
            private int point;
            private Membership membership;
        }
        @Getter
        @Setter
        public static class Detail {
            private Long memberId;
            private String username;
            private String companyName;
            private String businessNumber;
            private String presidentName;
            private String businessType;
            private String businessItem;
            private String realname;
            private String nickname;
            private MemberType type;
            private String email;
            private String phone;
            private int receiveEmailYn;
            private int receiveSmsYn;
            private int deniedYn;
            private int emailCertYn;
            private int level;
            private int point;
            private int totalPurchaseAmount;
            private Date lastLoggedinAt;
            private Role role = Role.ROLE_NOT_PERMITTED;
            private List<DeliveryAddress> deliveryAddress;
            private List<MemberCoupon> memberCoupon;
            private Membership membership;
        }
        @Getter
        @Setter
        public static class MemberCoupon {
            private Coupon coupon;
            private Date issueDate;
            private Date expirationDate;
        }
        @Getter
        @Setter
        public static class Coupon {
            private Long couponId;
            private String name;
            private String type;
            private int comparisonAmount;
            private int discountAmount;
            private int discountRate;
            private String validityPeriod;
        }
        @Getter
        @Setter
        public static class DeliveryAddress {
            private Long deliveryAddressId;
            private String name;
            private String recipientName;
            private String phone;
            private String tel;
            private String address1;
            private String address2;
            private String postalCode;
            private Boolean defaultYn;
        }
        @Getter
        @Setter
        public static class Membership {
            private Long membershipId;
            private String name;
            private int level;
            private Integer criteriaAbove;
            private Integer criteriaBelow;
            private int discountBenefit;
            private String pointBenefit;
        }
    }    
}