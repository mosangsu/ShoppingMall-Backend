package com.uniwear.backend.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "estimate_print_picture")
@IdClass(EstimatePrintPictureId.class)
public class EstimatePrintPicture {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="estimate_print_id")
    private EstimatePrint estimatePrint;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="picture_id")
    private Picture picture;
}

@Getter
@Setter
class EstimatePrintPictureId implements Serializable {

    private Long estimatePrint;
    private Long picture;

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