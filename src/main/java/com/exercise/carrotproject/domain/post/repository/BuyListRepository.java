package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.BuyList;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.SellList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyListRepository extends JpaRepository<BuyList,Long> {

    //for review
    List<BuyList> findByBuyer (Member buyer);
    BuyList findByPost (Post post);
}
