package com.uniwear.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.uniwear.backend.entity.member.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "estimate_product")
public class EstimateProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long estimateProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="estimate_id")
    private Estimate estimate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    private String printType;
    private String packageType;

    @OneToMany(mappedBy = "estimateProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateProductGroup> estimateProductGroups = new ArrayList<>();

    @OneToMany(mappedBy = "estimateProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimatePrint> estimatePrints = new ArrayList<>();

    public void addEstimateProductGroup(EstimateProductGroup epg) {
        estimateProductGroups.add(epg);
        epg.setEstimateProduct(this);
    }

    public void addEstimatePrint(EstimatePrint ep) {
        estimatePrints.add(ep);
        ep.setEstimateProduct(this);
    }

    @Builder
    public EstimateProduct (String printType, String packageType) {
        this.printType = printType;
        this.packageType = packageType;
    }
}