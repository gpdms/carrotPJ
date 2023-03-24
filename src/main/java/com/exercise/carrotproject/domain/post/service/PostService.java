package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Log4j2
public class PostService {

    @PersistenceContext
    EntityManager em;

    private final PostRepository postRepository;

    @Transactional
    public void insertPost(PostDto postDto){
        //Dto->Entity 변환
        Post post = Post.dtoToEntity(postDto);
        log.info("엔티티Post: "+post);

        em.persist(post);
    }


    public List<PostDto> selectAllPost(){
        //JPQL
        String sql = "select p from Post p order by p.postId desc";
        List<Post> postEntityList = em.createQuery(sql, Post.class).getResultList();

        //Entity리스트 -> Dto 리스트
        List<PostDto> postDtoList  = postEntityList.stream().map(PostDto::entityToDto).collect(Collectors.toList());
//        log.info("포스트 전체 정보: "+ postDtoList);

        return  postDtoList;
    }

    public Page<PostDto> paging(List<PostDto> postList, Pageable pageable){

        //페이징
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), postList.size());
        final Page<PostDto> page = new PageImpl<>(postList.subList(start, end), pageable, postList.size());
//        log.info("start:{}, end:{}, page:{}", start, end, page);
        return page;
    }

}
