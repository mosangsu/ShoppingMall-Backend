package com.uniwear.backend.repository;

import com.uniwear.backend.entity.PrintSize;

import org.springframework.data.repository.CrudRepository;

public interface PrintSizeRepository extends CrudRepository<PrintSize, Long> {
    PrintSize getById(Long printSizeId);
}