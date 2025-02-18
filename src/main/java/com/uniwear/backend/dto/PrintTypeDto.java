package com.uniwear.backend.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PrintTypeDto {
    
    @Getter
    @Setter
    public static class Res {
        private Long printTypeId;
        private String name;
        private boolean colorCustomizationYn;
        private int maxColorNumber;
        private Picture picture;
        private List<PrintColor> printColors;
        private List<PrintSize> printSizes;
    }
    
    @Getter
    @Setter
    public static class Picture {
        private String filename;
        private String path;
    }

    @Getter
    @Setter
    public static class PrintColor {
        private Color color;
    }

    @Getter
    @Setter
    public static class Color {
        private Long colorId;
        private Picture picture;
        private String name;
        private String rgbValue;
    }

    @Getter
    @Setter
    public static class PrintSize {
        private Long printSizeId;
        private String name;
        private String description;
        private int additionalCost;
        private Picture picture1;
        private Picture picture2;
    }
}