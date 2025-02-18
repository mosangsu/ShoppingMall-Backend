package com.uniwear.backend.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    private String packageType;

    private String printType;

    @OneToMany(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private List<OrderProductGroup> orderProductGroups = new ArrayList<>();

    public void addOrderProductGroup(OrderProductGroup opg) {
        orderProductGroups.add(opg);
        opg.setOrderProduct(this);
    }

    public OrderProduct (Cart cart) {
        this.product = cart.getProduct();
        this.packageType = cart.getPackageType();
        this.printType = cart.getPrintType();
    }
}
