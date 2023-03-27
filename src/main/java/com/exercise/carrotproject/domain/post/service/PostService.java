package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PostService {
    public void insertPost(PostDto postDto, MultipartFile[] uploadFiles) throws IOException;
    public void insertPostImg(Post postEntity, MultipartFile[] uploadFiles) throws IOException;
    public List<PostDto> selectAllPost();
    public PostDto selectOnePost(Long postId);
    public Page<PostDto> paging(List<PostDto> postList, Pageable pageable);


}
