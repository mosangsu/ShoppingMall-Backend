package com.uniwear.backend.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "print_type")
public class PrintType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long printTypeId;

    private String name;

    private String description;
    
    @Column(columnDefinition = "TINYINT")
    private boolean colorCustomizationYn;

    private int maxColorNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="picture_id")
    private Picture picture;

    @OneToMany(mappedBy = "printType")
    private List<PrintColor> printColors;

    @OneToMany(mappedBy = "printType")
    private List<PrintSize> printSizes;
}