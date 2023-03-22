package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import groovy.util.logging.Log4j2;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



@Service
//@RequiredArgsConstructor
@Log4j2 //왜 안되지???
public class PostService {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void insertPost(PostDto postDto){
        //Dto->Entity 변환
        Post post = Post.toEntity(postDto);
        System.out.println("엔티티Post: "+post);


        em.persist(post);

    }


}
