package com.uniwear.backend.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="option_group_id"),
        @JoinColumn(name="product_id")
    })
    private ProductOptionGroup productOptionGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_option_id")
    private ProductOption parentOption;

    private String name;
    private String value;
    private String value2;
    private int order;
    private int additionalCost;

    public Long getOptionGroupId () {
        return productOptionGroup != null ? productOptionGroup.getOptionGroupId() : null;
    }
}