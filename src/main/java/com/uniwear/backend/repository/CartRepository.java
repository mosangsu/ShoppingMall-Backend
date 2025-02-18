package com.uniwear.backend.repository;

import java.util.Collection;
import java.util.List;

import com.uniwear.backend.dto.CartDto.Res;
import com.uniwear.backend.entity.Cart;

import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> {
    List<Cart> findAllByMemberMemberId(Long memberId);
    List<Cart> findAllById(Iterable<Long> ids);
    List<Cart> findByCartIdInAndMemberMemberId(List<Long> ids, Long memberId);
    Cart findByProductProductIdAndMemberMemberIdAndPackageType(Long productId, Long memberId, String packageType);
    long countByMemberMemberId(Long memberId);
}