package com.uniwear.backend.repository;

import com.uniwear.backend.entity.Point;

import org.springframework.data.repository.CrudRepository;

public interface PointRepository extends CrudRepository<Point, Long> {
}