package com.uniwear.backend.repository;

import com.uniwear.backend.entity.member.Member;

import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByUsername(String username);
    Member getById(Long memberId);

}