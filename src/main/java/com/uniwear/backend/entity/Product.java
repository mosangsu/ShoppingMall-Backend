package com.uniwear.backend.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String code;
    private String supplierProductCode;
    private String name;
    private String supplierProductName;
    private String material;
    private int order;
    @Column(columnDefinition ="CHAR(10)")
    private String status;
    @Column(columnDefinition ="TEXT")
    private String summary;
    @Column(columnDefinition ="TEXT")
    private String content;
    private int price;
    private int salePrice;
    private int groupSalePrice;
    private int groupDiscountStandard;
    private int viewCount;
    private int sellCount;
    private int reviewCount;
    private int qnaCount;
    private int wishCount;
    @Column(columnDefinition ="DECIMAL(2,1)")
    private double ratingAverage;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;
    
    @OneToMany(mappedBy = "product")
    private Set<ProductTag> productTag;
    
    @OneToMany(mappedBy = "product")
    private Set<ProductPrintType> productPrintType;
    
    @OrderBy("sub_product_id asc")
    @OneToMany(mappedBy = "productId")
    private Set<SubProduct> subProduct;
    
    @OrderBy("order asc")
    @OneToMany(mappedBy = "product")
    private Set<ProductOptionGroup> productOptionGroup;
    
    @OneToMany(mappedBy = "product")
    private Set<Review> reviews;
    
    @OneToMany(mappedBy = "product")
    private Set<RelatedProduct> relatedProducts;
    
    @OneToMany(mappedBy = "product")
    private Set<ProductPicture> productPictures;
    
    @OneToMany(mappedBy = "product")
    private Set<ProductCategory> productCategories;
}