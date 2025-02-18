package com.uniwear.backend.dto;

import java.util.Date;
import java.util.List;

import com.uniwear.backend.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

public class OrderDto {
    @Getter
    @Setter
    public static class Req {
        private Long memberId;
        private List<Long> cartId;
        private Long couponId;
        private String receiptId;
        private int point;
        private String memo;
        private String status;
        private int totalPrice;
        private int pointDiscount;
        private int couponDiscount;
        private int deliveryCharge;
        private String payType;
        private String pg;
        private String tno;
        private String appNo;
        private String bankInfo;
        private String adminMemo;
        private String paymentTime;
        private String ordererName;
        private String ordererGroupName;
        private String ordererPhone;
        private String ordererEmail;
        private String recipientName;
        private String recipientTel;
        private String recipientPhone;
        private String address1;
        private String address2;
        private String postalCode;
        private Date hopedReleaseDate;
        private String deliveryMethod;
        private String deliveryMessage;
        private String odererName;
    }
    
    @Getter
    @Setter
    public static class Res {
        private Long orderId;
    
        private OrderStatus status;
        
        private int totalPrice;
        private int pointDiscount;
        private int couponDiscount;
        private int deliveryCharge;
        
        private String payType;
        private String pg;
        private String tno;
        private String appNo;
        private String bankInfo;
        private String receiptId;
        private String ordererName;
        private String ordererGroup_name;
        private String ordererPhone;
        private String ordererEmail;
        private String recipientName;
        private String recipientTel;
        private String recipientPhone;
        private String address1;
        private String address2;
        private String postalCode;
        private String deliveryMethod;
        private String deliveryMessage;
        private String memo;
        private String adminMemo;
        private String orderNumber;
        
        private Date hopedReleaseDate;
        private Date paymentTime;
        private Date createdAt;
        private List<OrderProduct> orderProducts;

        @Getter
        @Setter
        public static class OrderProduct {
            private Long orderProductId;
            private Product product;
            private String packageType;
            private String printType;
            private List<OrderProductGroup> orderProductGroups;
        }

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
        public static class OrderProductGroup {
            private int count;
            private List<OrderProductOption> orderProductOptions;
        }

        @Getter
        @Setter
        public static class OrderProductOption {
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

    public static class Count {
        @Getter
        @Setter
        public static class Res {
            private OrderStatus status;
            private Long count;
        }
    }
}