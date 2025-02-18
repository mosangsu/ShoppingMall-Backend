package com.uniwear.backend.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.uniwear.backend.entity.member.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "estimate_print_color")
@IdClass(EstimatePrintColorId.class)
public class EstimatePrintColor {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="estimate_print_id")
    private EstimatePrint estimatePrint;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="color_id")
    private Color color;
}

@Getter
@Setter
class EstimatePrintColorId implements Serializable {

    private Long estimatePrint;
    private Long color;

    @Override
    public boolean equals(Object arg0) {
        // TODO Auto-generated method stub
        return super.equals(arg0);
    }
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
}