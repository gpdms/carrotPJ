package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.SellList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellListRepository extends JpaRepository<SellList,Long> {

    //for review
    List<SellList> findBySeller (Member seller);
    SellList findByPost (Post post);
}
