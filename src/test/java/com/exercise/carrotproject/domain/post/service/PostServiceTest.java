package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import groovy.util.logging.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostRepository postRepository;
    @Test
    void selectPost() {
        List<Post> postList = postRepository.findAllByOrderByIdDesc();
        System.out.println("postList: " + postList);
    }
}