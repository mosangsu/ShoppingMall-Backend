package com.uniwear.backend.entity;

import javax.persistence.Column;
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
@Table(name = "print_position")
public class PrintPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long printPositionId;
    private String name;
    private String left;
    private String right;
    private String top;
    private String bottom;
    private String width;
    private String height;
    
    @Column(columnDefinition = "TINYINT")
    private boolean isCenter;
    @Column(columnDefinition = "TINYINT")
    private boolean isFront;
}
