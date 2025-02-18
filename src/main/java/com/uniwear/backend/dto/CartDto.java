package com.uniwear.backend.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class CartDto {
    @Getter
    @Setter
    public static class Req {
        private Long memberId;
        private Long productId;
        private String packageType;
        private String printType;
        private List<CartProductGroup> cartProductGroup;

        @Getter
        @Setter
        public static class CartProductGroup {
            private int count;
            private List<Long> cartProductOption;
        }
    }
    
    @Getter
    @Setter
    public static class Res {
        private Long cartId;
        private Long memberId;
        private Product product;
        private String packageType;
        private String printType;
        private List<CartProductGroup> cartProductGroup;

        @Getter
        @Setter
        public static class Product {
            private String name;
            private int salePrice;
            private int groupSalePrice;
            private int groupDiscountStandard;
            private Supplier supplier;
        }

        @Getter
        @Setter
        public static class Supplier {
            private Long supplierId;
            private String name;
            private String englishName;
            private String code;
            private String content;
            private String status;
        }

        @Getter
        @Setter
        public static class CartProductGroup {
            private int count;
            private List<CartProductOption> cartProductOption;
        }

        @Getter
        @Setter
        public static class CartProductOption {
            private ProductOption productOption;
        }

        @Getter
        @Setter
        public static class ProductOption {
            private Long productOptionId;
            private ProductOption parentOption;
            private String name;
            private int additionalCost;
        }
    }
}