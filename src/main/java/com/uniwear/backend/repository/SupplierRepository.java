package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.Supplier;

import org.springframework.data.repository.CrudRepository;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {
    List<Supplier> findAll();
}