package com.uniwear.backend.dto;

import lombok.Getter;
import lombok.Setter;

public class MembershipDto {
    @Getter
    @Setter
    public static class Req {
        private String username;
        private String password;
    }
    @Getter
    @Setter
    public static class Res {
        private Long membershipId;
        private String name;
        private int level;
        private Integer criteriaAbove;
        private Integer criteriaBelow;
        private int discountBenefit;
        private String pointBenefit;
    }    
}