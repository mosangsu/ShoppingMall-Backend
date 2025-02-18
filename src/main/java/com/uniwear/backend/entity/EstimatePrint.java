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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "estimate_print")
public class EstimatePrint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long estimatePrintId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="estimate_product_id")
    private EstimateProduct estimateProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="print_position_id")
    private PrintPosition printPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="print_type_id")
    private PrintType printType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="print_size_id")
    private PrintSize printSize;

    @Column(columnDefinition ="TEXT")
    private String detailedRequest;
    private int additionalCost;

    @OneToMany(mappedBy = "estimatePrint", cascade = CascadeType.ALL)
    private List<EstimatePrintColor> estimatePrintColors = new ArrayList<>();

    @OneToMany(mappedBy = "estimatePrint", cascade = CascadeType.ALL)
    private List<EstimatePrintPicture> estimatePrintPictures = new ArrayList<>();

    public void addEstimatePrintColor(EstimatePrintColor epg) {
        estimatePrintColors.add(epg);
        epg.setEstimatePrint(this);
    }

    public void addEstimatePrintPicture(EstimatePrintPicture epp) {
        estimatePrintPictures.add(epp);
        epp.setEstimatePrint(this);
    }

    @Builder
    public EstimatePrint (String detailedRequest, int additionalCost) {
        this.detailedRequest = detailedRequest;
        this.additionalCost = additionalCost;
    }
}