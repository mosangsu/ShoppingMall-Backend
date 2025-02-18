package com.uniwear.backend.entity;

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
@IdClass(CartProductOptionId.class)
@Table(name = "cart_product_option")
public class CartProductOption {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_product_group_id")
    private CartProductGroup cartProductGroup;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_option_id")
    private ProductOption productOption;
}
