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
@Table(name = "cart_product_group")
@NoArgsConstructor
public class CartProductGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartProductGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private int count;
    
    @OneToMany(mappedBy = "cartProductGroup", cascade = CascadeType.ALL)
    private List<CartProductOption> cartProductOption = new ArrayList<>();

    public CartProductGroup (Long cartProductGroupId) {
        this.cartProductGroupId = cartProductGroupId;
    }

    public void addCartProductOption(CartProductOption cpo) {
        cartProductOption.add(cpo);
        cpo.setCartProductGroup(this);
    }

    public void addAllCartProductOption(Collection<CartProductOption> cpo) {
        cartProductOption.addAll(cpo);
        for(CartProductOption item: cpo)
            item.setCartProductGroup(this);
    }
}
