package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.EstimatePrint;
import com.uniwear.backend.entity.EstimateProduct;

import org.springframework.data.repository.CrudRepository;

public interface EstimatePrintRepository extends CrudRepository<EstimatePrint, Long> {
  EstimatePrint getByEstimatePrintIdAndEstimateProductEstimateMemberMemberId(Long estimatePrintId, Long memberId);
}