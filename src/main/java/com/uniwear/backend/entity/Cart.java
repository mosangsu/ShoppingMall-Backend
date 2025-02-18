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

import com.uniwear.backend.entity.member.Member;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    private String packageType;

    private String printType;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartProductGroup> cartProductGroup = new ArrayList<>();

    public void addCartProductGroup(CartProductGroup cpg) {
        cartProductGroup.add(cpg);
        cpg.setCart(this);
    }

    public Cart (Member member, Product product, String packageType, String printType) {
        this.member = member;
        this.product = product;
        this.packageType = packageType;
        this.printType = printType;
    }
}
