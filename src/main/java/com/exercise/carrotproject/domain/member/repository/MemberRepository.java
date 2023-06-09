package com.exercise.carrotproject.domain.member.repository;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{
    boolean existsByEmail(String email);
    boolean existsByEmailAndRole(String email, Role role);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndRole(String email, Role role);
}
