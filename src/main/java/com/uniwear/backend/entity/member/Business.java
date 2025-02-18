package com.uniwear.backend.entity.member;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("B")
public class Business extends Member {

    private String companyName;
    private String businessNumber;
    private String presidentName;
    private String businessType;
    private String businessItem;
}