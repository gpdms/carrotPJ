package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.SellState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, CustomPostRepository {

    //포스트 갯수 메소드
    Long countByMember(Member member);
    //판매여부에 따른 포스트들 반환(hidestate=보임)
    //List<Post> findByMemberAndSellStateAndHideStateOrderByPostIdDesc(Member member, SellState sellState, HideState hideState);
    //판매중, 예약중인 포스트들 반환(hidestate=보임)
    List<Post> findByMemberAndHideStateAndSellStateOrSellStateOrderByPostIdDesc(Member member, HideState hideState, SellState onSale,SellState reservation);
    //숨김 포스트들 반환
    List<Post> findByMemberAndHideStateOrderByPostIdDesc(Member member, HideState hideState);


}
