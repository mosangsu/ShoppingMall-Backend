package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.Product;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Product getById(Long productId);
    List<Product> findAllByProductPicturesType(String type);
    List<Product> findAllByProductCategoriesCategoryCategoryIdAndProductPicturesType(Long categoryId, String type);
}