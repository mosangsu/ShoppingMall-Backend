package com.uniwear.backend.dto;

import lombok.Getter;
import lombok.Setter;

public class SupplierDto {
    
    @Getter
    @Setter
    public static class Res {
        private Long supplierId;
        private String name;
    }
}