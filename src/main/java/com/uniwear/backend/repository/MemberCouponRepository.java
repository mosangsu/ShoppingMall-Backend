package com.uniwear.backend.repository;

import com.uniwear.backend.entity.MemberCoupon;

import org.springframework.data.repository.CrudRepository;

public interface MemberCouponRepository extends CrudRepository<MemberCoupon, Long> {
    MemberCoupon findByMemberMemberIdAndCouponCouponId(Long memberId, Long couponId);
}