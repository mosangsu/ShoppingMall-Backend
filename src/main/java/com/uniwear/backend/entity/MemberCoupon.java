package com.uniwear.backend.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.uniwear.backend.entity.member.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member_coupon")
@IdClass(MemberCouponId.class)
public class MemberCoupon {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coupon_id")
    private Coupon coupon;
    
    @Temporal(TemporalType.DATE)
    private Date issueDate;
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
}