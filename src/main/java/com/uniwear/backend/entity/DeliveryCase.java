package com.uniwear.backend.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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

import com.uniwear.backend.enums.DeliveryCaseType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "delivery_case")
public class DeliveryCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryCaseId;

    private String title;

    @Convert(converter = DeliveryCaseType.DeliveryCaseTypeConverter.class)
    private DeliveryCaseType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @OneToMany(mappedBy = "deliveryCase", cascade = CascadeType.ALL)
    private List<DeliveryCasePicture> deliveryCasePictures;

    @OneToMany(mappedBy = "deliveryCase", cascade = CascadeType.ALL)
    private List<DeliveryCasePrintType> deliveryCasePrintTypes;

    @OneToMany(mappedBy = "deliveryCase", cascade = CascadeType.ALL)
    private List<DeliveryCaseTag> deliveryCaseTags;
}
