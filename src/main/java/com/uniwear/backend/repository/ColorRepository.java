package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.Color;

import org.springframework.data.repository.CrudRepository;

public interface ColorRepository extends CrudRepository<Color, Long> {
    List<Color> findAllById(Iterable<Long> ids);
}