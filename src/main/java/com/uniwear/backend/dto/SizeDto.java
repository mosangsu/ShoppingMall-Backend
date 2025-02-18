package com.uniwear.backend.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class SizeDto {
    
    @Getter
    @Setter
    public static class Res {
        private Long printSizeId;
        private String name;
        private String description;
        private int additionalCost;
        private Picture picture1;
        private Picture picture2;
    }

    @Getter
    @Setter
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
}