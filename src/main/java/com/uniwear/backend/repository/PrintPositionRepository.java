package com.uniwear.backend.repository;

import com.uniwear.backend.entity.PrintPosition;

import org.springframework.data.repository.CrudRepository;

public interface PrintPositionRepository extends CrudRepository<PrintPosition, Long> {
    PrintPosition getById(Long printPositionId);
}