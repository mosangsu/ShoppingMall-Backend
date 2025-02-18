package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.Estimate;

import org.springframework.data.repository.CrudRepository;

public interface EstimateRepository extends CrudRepository<Estimate, Long> {
  List<Estimate> findAllByMemberMemberIdOrderByCreatedAtDesc(Long memberId);
  Estimate getById(Long memberId);
}