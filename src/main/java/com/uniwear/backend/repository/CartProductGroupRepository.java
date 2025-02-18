package com.uniwear.backend.repository;

import java.util.Collection;

import com.uniwear.backend.entity.CartProductGroup;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CartProductGroupRepository extends CrudRepository<CartProductGroup, Long> {

    @Query(value="SELECT cpg.* " +
            "FROM " +
            "cart_product_group cpg " +
            "INNER JOIN (SELECT cart_product_group_id, count(*) cnt FROM cart_product_option WHERE product_option_id IN :ids " +
            "GROUP BY cart_product_group_id " +
            ") cpo ON cpg.cart_product_group_id = cpo.cart_product_group_id " +
            "WHERE cpg.cart_id = :cartId AND cpo.cnt = :cnt", nativeQuery=true)
    CartProductGroup findByCartIdAndProductOptionIds(@Param("cartId") Long cartId, @Param("ids") Collection<Long> ids, @Param("cnt") int cnt);
}