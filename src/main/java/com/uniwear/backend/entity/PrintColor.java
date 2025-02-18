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
@Table(name = "print_color")
public class PrintColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long printColorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="print_type_id")
    private PrintType printType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="color_id")
    private Color color;
}
