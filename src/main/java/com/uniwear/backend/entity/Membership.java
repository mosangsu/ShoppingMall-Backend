package com.uniwear.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "membership")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    private String name;
    private int level;
    private Integer criteriaAbove;
    private Integer criteriaBelow;
    @Column(columnDefinition = "TINYINT")
    private int discountBenefit;
    @Column(columnDefinition = "TINYINT")
    private String pointBenefit;
}