package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.PrintType;

import org.springframework.data.repository.CrudRepository;

public interface PrintTypeRepository extends CrudRepository<PrintType, Long> {
    List<PrintType> findAll();
    PrintType getById(Long printTypeId);
}