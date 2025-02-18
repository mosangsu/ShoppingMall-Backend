package com.uniwear.backend.service;

import org.springframework.transaction.annotation.Transactional;

import com.uniwear.backend.dto.DeliveryCaseDto;
import com.uniwear.backend.repository.DeliveryCaseRepository;
import com.uniwear.backend.repository.jpql.JDeliveryCaseRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeliveryCaseService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DeliveryCaseRepository deliveryCaseRepository;

    @Autowired
    private JDeliveryCaseRepository jDeliveryCaseRepository;

    public Page<DeliveryCaseDto.Summary.Res> getDeliveryCaseList(Long printTypeId, String caseType, Pageable pageable) {
        return jDeliveryCaseRepository.findAll(printTypeId, caseType, pageable);
    }

    public DeliveryCaseDto.Detail.Res getDeliveryCaseDetail(Long deliveryCaseId) {
        return modelMapper.map(deliveryCaseRepository.findById(deliveryCaseId).get(), DeliveryCaseDto.Detail.Res.class);
    }
}