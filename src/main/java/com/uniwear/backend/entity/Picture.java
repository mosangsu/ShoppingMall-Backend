package com.uniwear.backend.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "picture")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pictureId;

    private String name;
    private String filename;
    private String path;
    private int filesize;
    private int width;
    private int height;
    private Date createdAt;

    @Builder
    public Picture (String name, String filename, String path, int filesize, int width, int height, Date createdAt) {
        this.name = name;
        this.filename = filename;
        this.path = path;
        this.filesize = filesize;
        this.width = width;
        this.height = height;
        this.createdAt = createdAt;
    }
}