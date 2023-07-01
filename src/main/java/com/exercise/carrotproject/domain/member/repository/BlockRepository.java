package com.exercise.carrotproject.domain.member.repository;

import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long>, BlockCustomRepository {
    Optional<Block> findByFromMemAndToMem(Member fromMem, Member toMem);
}
