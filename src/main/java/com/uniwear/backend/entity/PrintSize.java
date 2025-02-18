package com.uniwear.backend.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "print_size")
public class PrintSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long printSizeId;
    private String name;
    private String description;
    private int additionalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="print_type_id")
    private PrintType printType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="picture1_id")
    private Picture picture1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="picture2_id")
    private Picture picture2;
}
