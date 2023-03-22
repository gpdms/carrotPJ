package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;




//@RequiredArgsConstructor
@Service
@Log4j2 //왜 안되지???
public class PostService {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void insertPost(PostDto postDto){
        //Dto->Entity 변환
        Post post = Post.toEntity(postDto);
        log.info("엔티티Post: "+post);


        em.persist(post);

    }


}