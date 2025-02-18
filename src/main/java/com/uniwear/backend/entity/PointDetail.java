package com.uniwear.backend.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "point_detail")
public class PointDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_detail_group_id")
    private PointDetail pointDetailGroup;

    private String code;
    private int amount;
    private Date createdAt;
}
