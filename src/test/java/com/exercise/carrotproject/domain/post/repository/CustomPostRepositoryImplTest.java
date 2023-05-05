package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CustomPostRepositoryImplTest {
 @Autowired
 CustomPostRepositoryImpl postRepository;

 @Test
 void soldList(){
     List<SoldPostDto> soldPosts = postRepository.getSoldList("tester2");
     for (SoldPostDto soldPost : soldPosts) {
         if(soldPost.getReviewBuyerId() != null)
             System.out.println("soldPost.getReviewBuyerId() = " + soldPost.getReviewBuyerId());
         else
             System.out.println("soldPost.getReviewBuyerId() = " + "널값");
     }
 }
}