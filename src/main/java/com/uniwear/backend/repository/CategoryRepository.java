package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.Category;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findAll();
    List<Category> findByParentCategoryIsNull();
}