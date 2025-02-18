package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.EstimatePrint;
import com.uniwear.backend.entity.EstimateProduct;
import com.uniwear.backend.entity.EstimateProductGroup;

import org.springframework.data.repository.CrudRepository;

public interface EstimateProductGroupRepository extends CrudRepository<EstimateProductGroup, Long> {
  EstimateProductGroup getByEstimateProductGroupIdAndEstimateProductEstimateMemberMemberId(Long estimateProductGroupId, Long memberId);
}