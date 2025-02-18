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
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
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
@Table(name = "estimate_product_group")
public class EstimateProductGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long estimateProductGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="estimate_product_id")
    private EstimateProduct estimateProduct;
    
    private int count;

    @OrderColumn(name = "`order`")
    @OneToMany(mappedBy = "estimateProductGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateProductOption> estimateProductOptions = new ArrayList<>();

    public void addEstimateProductOption(EstimateProductOption epo) {
        estimateProductOptions.add(epo);
        epo.setEstimateProductGroup(this);
    }

    @Builder
    public EstimateProductGroup (int count) {
        this.count = count;
    }
}