package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.SellState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import com.exercise.carrotproject.domain.post.entity.QPost;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageServiceImpl {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


//    @Override
    public Map selectPostBySellState(String memId){
        Member member = memberRepository.findById(memId).orElseThrow();

        //판매중,예약중
        List<Post> onSalePostList = postRepository.findByMemberAndHideStateAndSellStateOrSellStateOrderByPostIdDesc(member, HideState.SHOW, SellState.ON_SALE,SellState.RESERVATION);
        //entity리스트->dto리스트
        List<PostDto> postDtoOnSaleList = PostEntityDtoMapper.toDtoList(onSalePostList);


        //판매완료
        List<Post> soldPostList = postRepository.findByMemberAndSellStateAndHideStateOrderByPostIdDesc(member, SellState.SOLD, HideState.SHOW);
        //entity리스트->dto리스트
        List<PostDto> postDtoSoldList = PostEntityDtoMapper.toDtoList(soldPostList);

        Map map = new HashMap();
        map.put("onSaleAndRsvList", postDtoOnSaleList);
        map.put("soldList", postDtoSoldList);

        return map;
    }

//    @Override
    public List<PostDto> selectHidePost(String memId){
        Member member = memberRepository.findById(memId).orElseThrow();

        List<Post> postList = postRepository.findByMemberAndHideStateOrderByPostIdDesc(member,HideState.HIDE);
        //entity리스트->dto리스트
        List<PostDto> postDtoList = PostEntityDtoMapper.toDtoList(postList);

        return postDtoList;
    }

}
