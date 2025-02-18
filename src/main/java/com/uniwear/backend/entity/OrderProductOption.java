package com.uniwear.backend.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(OrderProductOptionId.class)
@Table(name = "order_product_option")
public class OrderProductOption {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_product_group_id")
    private OrderProductGroup orderProductGroup;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_option_id")
    private ProductOption productOption;

    public OrderProductOption (CartProductOption cpo) {
        this.productOption = cpo.getProductOption();
    }
}
