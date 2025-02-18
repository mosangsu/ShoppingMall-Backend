package com.uniwear.backend.service;

import org.springframework.transaction.annotation.Transactional;

import com.uniwear.backend.dto.MemberDto;
import com.uniwear.backend.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MemberService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MemberRepository memberRepository;

    public MemberDto.Res.Detail getMember(Long id) {
        return modelMapper.map(memberRepository.findById(id).get(), MemberDto.Res.Detail.class);
    }
}