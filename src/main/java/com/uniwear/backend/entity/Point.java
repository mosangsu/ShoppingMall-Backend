package com.uniwear.backend.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.uniwear.backend.entity.member.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;
    private int amount;
    private String orderNumber;
    private Date createdAt;
    private Date expirationDate;
    
    @OneToMany(mappedBy = "point", cascade = CascadeType.ALL)
    private List<PointDetail> pointDetails;
}
