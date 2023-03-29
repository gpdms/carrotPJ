package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PostService {
    void insertPost(PostDto postDto, MultipartFile[] uploadFiles) throws IOException;
    void insertPostImg(Post postEntity, MultipartFile[] uploadFiles) throws IOException;
    List<PostDto> selectAllPost();
    PostDto selectOnePost(Long postId);
    Page<PostDto> paging(List<PostDto> postList, Pageable pageable);
    List<PostImgDto> selectPostImgs(Long postId);
    PostImgDto selectOnePostImg(Long imgId);

}
