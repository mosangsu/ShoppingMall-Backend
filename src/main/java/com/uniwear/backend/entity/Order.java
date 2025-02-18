package com.uniwear.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.uniwear.backend.entity.member.Member;
import com.uniwear.backend.enums.OrderStatus;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Convert(converter = OrderStatus.OrderStatusConverter.class)
    private OrderStatus status;
    
    private int totalPrice;
    private int pointDiscount;
    private int couponDiscount;
    private int deliveryCharge;
    
    private String payType;
    private String pg;
    private String tno;
    private String appNo;
    private String bankInfo;
    private String receiptId;
    private String ordererName;
    private String ordererGroup_name;
    private String ordererPhone;
    private String ordererEmail;
    private String recipientName;
    private String recipientTel;
    private String recipientPhone;
    private String address1;
    private String address2;
    private String postalCode;
    private String deliveryMethod;
    private String deliveryMessage;
    @Column(columnDefinition ="TEXT")
    private String memo;
    @Column(columnDefinition ="TEXT")
    private String adminMemo;
    private String orderNumber;
    
    @Temporal(TemporalType.DATE)
    private Date hopedReleaseDate;
    private Date paymentTime;
    private Date createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public void addOrderProduct(OrderProduct op) {
        orderProducts.add(op);
        op.setOrder(this);
    }
}
