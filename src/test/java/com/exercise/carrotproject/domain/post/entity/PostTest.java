package com.exercise.carrotproject.domain.post.entity;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostTest {

    @Autowired
    PostRepository postRepository;
    @Test
    @Transactional
    @Rollback(value = false)
    public void postIns(){
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .category(Category.BEAUTY)
                .build();
        postRepository.save(post);

    }
}