package com.uniwear.backend.entity;

import java.util.ArrayList;
import java.util.Collection;
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
@Table(name = "order_product_group")
public class OrderProductGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    private int count;
    
    @OneToMany(mappedBy = "orderProductGroup", cascade = CascadeType.ALL)
    private List<OrderProductOption> orderProductOptions = new ArrayList<>();

    public void addOrderProductOption(OrderProductOption opo) {
        orderProductOptions.add(opo);
        opo.setOrderProductGroup(this);
    }

    public void addAllOrderProductOption(Collection<OrderProductOption> opo) {
        orderProductOptions.addAll(opo);
        for(OrderProductOption item: opo)
            item.setOrderProductGroup(this);
    }

    public OrderProductGroup (CartProductGroup cpg) {
        this.count = cpg.getCount();
    }
}
