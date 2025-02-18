package com.uniwear.backend.entity;

import java.util.List;

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
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String name;
    private String value;
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy="parentCategory")
    private List<Category> categories;

    public Long getParentCategoryId () {
        return parentCategory != null ? parentCategory.getCategoryId() : null;
    }
}