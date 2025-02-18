package com.uniwear.backend.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "delivery_case_tag")
@IdClass(DeliveryCaseTagId.class)
public class DeliveryCaseTag {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="delivery_case_id")
    private DeliveryCase deliveryCase;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tag_id")
    private Tag tag;
}
