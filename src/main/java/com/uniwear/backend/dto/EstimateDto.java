package com.uniwear.backend.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.uniwear.backend.entity.Estimate;
import com.uniwear.backend.entity.EstimatePrint;
import com.uniwear.backend.entity.EstimateProduct;
import com.uniwear.backend.entity.EstimateProductGroup;
import com.uniwear.backend.enums.EstimateStatus;

import lombok.Getter;
import lombok.Setter;

public class EstimateDto {
    @Getter
    @Setter
    public static class Req {
        private String title;
        private String writer;
        private String groupName;
        private String phone;
        private String email;
        private String password;
        private String content;
        private boolean productYn;
        private boolean tempYn;
        private List<EstimateProductDto> estimateProducts;
        // private List<String> pictures;
        
        public static Estimate toEntity (EstimateDto.Req estimateDto) {
            return Estimate.builder()
                .title(estimateDto.getTitle())
                .writer(estimateDto.getWriter())
                .groupName(estimateDto.getGroupName())
                .phone(estimateDto.getPhone())
                .email(estimateDto.getEmail())
                .password(estimateDto.getPassword())
                .productYn(estimateDto.isProductYn())
                .content(estimateDto.getContent())
                .build();
        }
        
        public static void toEntity (EstimateDto.Req estimateDto, Estimate estimate) {
            estimate.setTitle(estimateDto.getTitle());
            estimate.setWriter(estimateDto.getWriter());
            estimate.setGroupName(estimateDto.getGroupName());
            estimate.setPhone(estimateDto.getPhone());
            estimate.setEmail(estimateDto.getEmail());
            estimate.setPassword(estimateDto.getPassword());
            estimate.setProductYn(estimateDto.isProductYn());
            estimate.setContent(estimateDto.getContent());
        }

        @Getter
        @Setter
        public static class EstimateProductDto {
            private Long estimateProductId;
            private ProductDto product;
            private String packageType;
            private String printType;

            private List<EstimatePrintDto> estimatePrints;
            private List<EstimateProductGroupDto> estimateProductGroups;
        
            public static EstimateProduct toEntity (EstimateProductDto estimateProductDto) {
                return EstimateProduct.builder()
                    .packageType(estimateProductDto.getPackageType())
                    .printType(estimateProductDto.getPrintType())
                    .build();
            }
        
            public static void toEntity (EstimateProductDto estimateProductDto, EstimateProduct estimateProduct) {
                estimateProduct.setPackageType(estimateProductDto.getPackageType());
                estimateProduct.setPrintType(estimateProductDto.getPrintType());
            }
        }

        @Getter
        @Setter
        public static class ProductDto {
            private Long productId;
        }

        @Getter
        @Setter
        public static class EstimatePrintDto {
            private Long estimatePrintId;
            private PrintPositionDto printPosition;
            private PrintTypeDto printType;
            private PrintSizeDto printSize;

            private int additionalCost;
            private String detailedRequest;
            
            private List<EstimatePrintColorDto> estimatePrintColors;
            private List<EstimatePrintPictureDto> estimatePrintPictures;
        
            public static EstimatePrint toEntity (EstimatePrintDto estimatePrintDto) {
                return EstimatePrint.builder()
                    .additionalCost(estimatePrintDto.getAdditionalCost())
                    .detailedRequest(estimatePrintDto.getDetailedRequest())
                    .build();
            }
        
            public static void toEntity (EstimatePrintDto estimatePrintDto, EstimatePrint estimatePrint) {
                estimatePrint.setAdditionalCost(estimatePrintDto.getAdditionalCost());
                estimatePrint.setDetailedRequest(estimatePrintDto.getDetailedRequest());
            }
        }

        @Getter
        @Setter
        public static class PrintPositionDto {
            private Long printPositionId;
        }

        @Getter
        @Setter
        public static class PrintTypeDto {
            private Long printTypeId;
        }

        @Getter
        @Setter
        public static class PrintSizeDto {
            private Long printSizeId;
        }

        @Getter
        @Setter
        public static class EstimatePrintColorDto {
            private ColorDto color;
        }

        @Getter
        @Setter
        public static class ColorDto {
            private Long colorId;
        }

        @Getter
        @Setter
        public static class EstimatePrintPictureDto {
            private PictureDto picture;
            private FileDto fileData;
        }

        @Getter
        @Setter
        public static class FileDto {
            private String name;
            private String originalName;
            private int size;
            private String type;
            private Date lastModified;
        }

        @Getter
        @Setter
        public static class PictureDto {
            private Long pictureId;
            private String name;
            private String filename;
        }

        @Getter
        @Setter
        public static class EstimateProductGroupDto {
            private Long estimateProductGroupId;
            private int count;
            private List<Long> estimateProductOptions;
        
            public static EstimateProductGroup toEntity (EstimateProductGroupDto estimateProductGroupDto) {
                return EstimateProductGroup.builder()
                    .count(estimateProductGroupDto.getCount())
                    .build();
            }
        
            public static void toEntity (EstimateProductGroupDto estimateProductGroupDto, EstimateProductGroup estimateProductGroup) {
                estimateProductGroup.setCount(estimateProductGroupDto.getCount());
            }
        }

        @Getter
        @Setter
        public static class ProductOptionDto {
            private Long productOptionId;
        }
    }
    @Getter
    @Setter
    public static class Res {
        private Long estimateId;
        private String title;
        private String writer;
        private Date createdAt;
        private EstimateStatus status;
        private List<EstimateProductDto> estimateProducts;
        private Long count;
        
        @Getter
        @Setter
        public static class EstimateProductDto {
            private ProductDto product;

            @Getter
            @Setter
            public static class ProductDto {
                private Long productId;
                private String name;
                private SupplierDto supplier;
                private List<ProductPictureDto> productPictures;

                @Getter
                @Setter
                public static class SupplierDto {
                    private String name;
                }
        
                @Getter
                @Setter
                public static class ProductPictureDto {
                    private PictureDto picture;
    
                    @Getter
                    @Setter
                    public static class PictureDto {
                        private String filename;
                        private String path;
                    }
                }
            }
        }
    }

    public static class Detail {
        @Getter
        @Setter
        public static class Res {
            private String title;
            private String writer;
            private String groupName;
            private String phone;
            private String email;
            private String password;
            private String content;
            private boolean productYn;
            private List<EstimateProductDto> estimateProducts;
            // private List<String> pictures;

            @Getter
            @Setter
            public static class EstimateProductDto {
                private Long estimateProductId;
                private ProductDto product;
                private String packageType;
                private String printType;

                private List<EstimatePrintDto> estimatePrints;
                private List<EstimateProductGroupDto> estimateProductGroups;

                @Getter
                @Setter
                public static class ProductDto {
                    private Long productId;
                    private String name;
                    private SupplierDto supplier;
                    private int groupSalePrice;
                    private int salePrice;

                    @Getter
                    @Setter
                    public static class SupplierDto {
                        private String name;
                    }
                }

                @Getter
                @Setter
                public static class EstimatePrintDto {
                    private Long estimatePrintId;
                    private PrintPositionDto printPosition;
                    private PrintTypeDto printType;
                    private PrintSizeDto printSize;
    
                    private int additionalCost;
                    private String detailedRequest;
                    
                    private List<EstimatePrintColorDto> estimatePrintColors;
                    private List<EstimatePrintPictureDto> estimatePrintPictures;

                    @Getter
                    @Setter
                    public static class PrintPositionDto {
                        private Long printPositionId;
                        private String name;
                    }

                    @Getter
                    @Setter
                    public static class PrintTypeDto {
                        private Long printTypeId;
                        private String name;
                    }

                    @Getter
                    @Setter
                    public static class PrintSizeDto {
                        private Long printSizeId;
                        private String name;
                        private String description;
                    }

                    @Getter
                    @Setter
                    public static class EstimatePrintColorDto {
                        private ColorDto color;

                        @Getter
                        @Setter
                        public static class ColorDto {
                            private Long colorId;
                            private String rgbValue;
                            private PictureDto picture;
                        }
                    }

                    @Getter
                    @Setter
                    public static class EstimatePrintPictureDto {
                        private PictureDto picture;
                    }

                    @Getter
                    @Setter
                    public static class PictureDto {
                        private Long pictureId;
                        private String name;
                        private String filename;
                        private String filesize;
                    }
                }

                @Getter
                @Setter
                public static class EstimateProductGroupDto {
                    private Long estimateProductGroupId;
                    private int count;
                    private List<EstimateProductOptionDto> estimateProductOptions;
    
                    @Getter
                    @Setter
                    public static class EstimateProductOptionDto {
                        private ProductOptionDto productOption;
    
                        @Getter
                        @Setter
                        public static class ProductOptionDto {
                            private Long productOptionId;
                            private Long optionGroupId;
                            private ProductOptionDto parentOption;
                            private String name;
                            private String value;
                            private String value2;
                            private int order;
                            private int additionalCost;
                        }
                    }
                }
            }
        }
    }

    public static class Count {
        @Getter
        @Setter
        public static class Res {
            private EstimateStatus status;
            private Long count;
        }
    }
}