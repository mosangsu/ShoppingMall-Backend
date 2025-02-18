package com.uniwear.backend.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ProductDto {
    public static class Summary {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private Long productId;
            private String name;
            private int order;
            private int salePrice;
            private int groupSalePrice;
            private int reviewCount;
            private Date createdAt;
            private Date updatedAt;
            private Supplier supplier;
            private List<ProductPicture> productPictures;

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Supplier {
                private String name;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ProductPicture {
                private Picture picture;
                private String name;
                private String type;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Picture {
                private String filename;
                private String path;
            }
        }
    }

    public static class Detail {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private Long productId;
            private String code;
            private String supplierProductCode;
            private String name;
            private String supplierProductName;
            private String material;
            private int order;
            private String status;
            private String summary;
            private String content;
            private int price;
            private int salePrice;
            private int groupSalePrice;
            private int groupDiscountStandard;
            private int viewCount;
            private int sellCount;
            private int reviewCount;
            private int qnaCount;
            private int wishCount;
            private double ratingAverage;
            private Date createdAt;
            private Date updatedAt;
            private Supplier supplier;
            private Category category;
            private List<ProductTag> productTag;
            private List<ProductPrintType> productPrintType;
            private List<SubProduct> subProduct;
            private List<ProductOptionGroup> productOptionGroup;
            private List<Review> reviews;
            private List<RelatedProduct> relatedProducts;
            private List<ProductPicture> productPictures;

            @Getter
            @Setter
            @NoArgsConstructor
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
            @NoArgsConstructor
            public static class Category {
                private Long categoryId;
                private String name;
                private String value;
                private int order;
                private Category parentCategory;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ProductTag {
                private Tag tag;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Tag {
                private Long tagId;
                private String name;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ProductPrintType {
                private PrintType printType;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class PrintType {
                private Long printTypeId;
                private String name;
                private String description;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class SubProduct {
                private Long subProductId;
                private Long productId;
                private String name;
                private int price;
                private int stock;
                private Date createdAt;
                private Date updatedAt;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ProductOptionGroup {
                private Long optionGroupId;
                private String name;
                private String type;
                private int order;
                private List<ProductOption> productOption;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ProductOption {
                private Long productOptionId;
                private Long optionGroupId;
                private ProductOption parentOption;
                private String name;
                private String value;
                private String value2;
                private int order;
                private int additionalCost;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Review {
                private Long reviewId;
                private Member member;
                private String title;
                private String content;
                private String productOptions;
                private int rating;
                private Date createdAt;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Member {
                private String realname;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class RelatedProduct {
                private Product relatedProduct;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class ProductPicture {
                private Picture picture;
                private String name;
                private String type;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Picture {
                private Long pictureId;
                private String name;
                private String filename;
                private String path;
                private int filesize;
                private int width;
                private int height;
                private Date createdAt;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Product {
                private Long productId;
                private String code;
                private String supplierProductCode;
                private String name;
                private String supplierProductName;
                private String material;
                private int order;
                private String status;
                private String summary;
                private String content;
                private int price;
                private int salePrice;
                private int groupSalePrice;
                private int groupDiscountStandard;
                private int viewCount;
                private int sellCount;
                private int reviewCount;
                private int qnaCount;
                private int wishCount;
                private double ratingAverage;
                private Date createdAt;
                private Date updatedAt;
                private Supplier supplier;
                private Category category;
                private List<ProductTag> productTag;
                private List<ProductPrintType> productPrintType;
                private List<SubProduct> subProduct;
                private List<ProductOptionGroup> productOptionGroup;
                private List<Review> reviews;
            }
        }
    }

    public static class Option {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private Long productId;
            private String name;
            private int salePrice;
            private int groupSalePrice;
            private int groupDiscountStandard;
            private Supplier supplier;
            private List<ProductPrintType> productPrintType;
            private List<ProductOptionGroup> productOptionGroup;
            private List<SubProduct> subProduct;
            // private List<ProductPicture> productPictures;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Supplier {
            private String name;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ProductPrintType {
            private PrintType printType;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class PrintType {
            private Long printTypeId;
            private String name;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ProductOptionGroup {
            private Long optionGroupId;
            private String name;
            private String type;
            private int order;
            private List<ProductOption> productOption;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ProductOption {
            private Long productOptionId;
            private Long optionGroupId;
            private ProductOption parentOption;
            private String name;
            private String value;
            private String value2;
            private int order;
            private int additionalCost;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class SubProduct {
            private Long subProductId;
            private Long productId;
            private String name;
            private int price;
            private int stock;
            private Date createdAt;
            private Date updatedAt;
        }
    }
}