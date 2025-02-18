package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.EstimateProduct;

import org.springframework.data.repository.CrudRepository;

public interface EstimateProductRepository extends CrudRepository<EstimateProduct, Long> {
  EstimateProduct getByEstimateProductIdAndEstimateMemberMemberId(Long estimateProductId, Long memberId);
}