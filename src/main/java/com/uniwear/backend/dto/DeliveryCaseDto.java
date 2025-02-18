package com.uniwear.backend.dto;

import java.util.List;

import com.uniwear.backend.enums.DeliveryCaseType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DeliveryCaseDto {
    public static class Summary {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private Long deliveryCaseId;
            private String title;
            private DeliveryCaseType type;
            private List<DeliveryCasePicture> deliveryCasePictures;
            private List<DeliveryCasePrintType> deliveryCasePrintTypes;

            @Getter
            @Setter
            @NoArgsConstructor
            public static class DeliveryCasePicture {
                private Picture picture;
                private String type;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Picture {
                private String filename;
                private String path;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class DeliveryCasePrintType {
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
        }
    }

    public static class Detail {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private Long deliveryCaseId;
            private String title;
            private DeliveryCaseType type;
            private Product product;
            private List<DeliveryCasePicture> deliveryCasePictures;
            private List<DeliveryCasePrintType> deliveryCasePrintTypes;
            private List<DeliveryCaseTag> deliveryCaseTags;

            @Getter
            @Setter
            @NoArgsConstructor
            public static class DeliveryCasePicture {
                private Picture picture;
                private String type;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Picture {
                private String filename;
                private String path;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            public static class DeliveryCasePrintType {
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
            public static class DeliveryCaseTag {
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
            public static class Product {
                private Long productId;
                private String name;
                private int salePrice;
                private int groupSalePrice;
                private Supplier supplier;
                private List<ProductPicture> productPictures;
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
            public static class Supplier {
                private String name;
            }
        }
    }
}
