package com.uniwear.backend.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionGroupId implements Serializable {

    private Long optionGroupId;
    private Long product;
}