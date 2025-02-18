package com.uniwear.backend.dto;

import lombok.Getter;
import lombok.Setter;

public class ColorDto {
    
    @Getter
    @Setter
    public static class Res {
        private Long colorId;
        private Picture picture;
        private String name;
        private String rgbValue;
    }
    
    @Getter
    @Setter
    public static class Picture {
        private String filename;
        private String path;
    }
}