package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.Membership;
import com.uniwear.backend.entity.member.Member;

import org.springframework.data.repository.CrudRepository;

public interface MembershipRepository extends CrudRepository<Membership, Long> {
    List<Membership> findAll();
}