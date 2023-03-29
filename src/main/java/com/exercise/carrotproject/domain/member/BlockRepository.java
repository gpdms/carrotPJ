package com.exercise.carrotproject.domain.member;

import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findByFromMemAndToMem(Member fromMem, Member toMem);
    //boolean existsBlockByFromMemAndToMem(Member fromMem, Member toMem);
}
