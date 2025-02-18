package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.ProductOption;

import org.springframework.data.repository.CrudRepository;

public interface ProductOptionRepository extends CrudRepository<ProductOption, Long> {
    List<ProductOption> findAllById(Iterable<Long> ids);
    ProductOption getById(Long productOptionId);
}