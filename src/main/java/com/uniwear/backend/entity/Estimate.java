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
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.uniwear.backend.entity.member.Member;
import com.uniwear.backend.enums.EstimateStatus;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "estimate")
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long estimateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Convert(converter = EstimateStatus.EstimateStatusConverter.class)
    private EstimateStatus status;

    private String title;
    private String writer;
    private String groupName;
    private String phone;
    private String email;
    private String password;
    @Column(columnDefinition = "TINYINT")
    private boolean productYn;
    @Column(columnDefinition ="TEXT")
    private String content;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL)
    private List<EstimateProduct> estimateProducts = new ArrayList<>();

    public void addEstimateProduct(EstimateProduct ep) {
        estimateProducts.add(ep);
        ep.setEstimate(this);
    }

    @Builder
    public Estimate (String title, String writer, String groupName, String phone, String email, String password, String content, boolean productYn) {
        this.title = title;
        this.writer = writer;
        this.groupName = groupName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.productYn = productYn;
        this.content = content;
    }
}