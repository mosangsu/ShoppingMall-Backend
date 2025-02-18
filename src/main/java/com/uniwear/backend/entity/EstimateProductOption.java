package com.uniwear.backend.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "estimate_product_option")
@IdClass(EstimateProductOptionId.class)
public class EstimateProductOption {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="estimate_product_group_id")
    private EstimateProductGroup estimateProductGroup;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_option_id")
    private ProductOption productOption;
}

@Getter
@Setter
class EstimateProductOptionId implements Serializable {

    private Long estimateProductGroup;
    private Long productOption;

    @Override
    public boolean equals(Object arg0) {
        // TODO Auto-generated method stub
        return super.equals(arg0);
    }
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
}