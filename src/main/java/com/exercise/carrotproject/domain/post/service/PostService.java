package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.chat.dto.ChatRoomDto;
import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.dto.MtPlaceDto;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.PostImgDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface PostService {
    String insertPost(PostDto postDto, MultipartFile[] uploadFiles, MtPlaceDto mtPlaceDto) throws IOException;
    void insertPostImg(Post postEntity, MultipartFile[] uploadFiles) throws IOException;
    void insertMtPlace(Post postEntity, MtPlaceDto mtPlaceDto);
    void updateMtPlace(PostDto postDto, MtPlaceDto mtPlaceDto);
    void deleteMtPlace(Post post);
    List<PostDto> selectAllPost(MemberDto memberDto);
    Page<PostDto> paging(List<PostDto> postList, Pageable pageable);
    PostDto selectOnePost(Long postId);
    List<PostImgDto> selectPostImgs(Long postId);
    PostImgDto selectOnePostImg(Long imgId);
    MtPlaceDto selectMtPlace(Long postId);
    void deleteOnePostImg(Long imgId);
    void updatePost(PostDto postDto, MultipartFile[] uploadFiles) throws IOException;
    void deletePost(Long postId);
    String updateHideState(Long postId, String hideStateName);
    String updateSellState(Long postId, String sellStateName);
    Map selectPostBySellState(String memId);
    List<PostDto> selectHidePost(String memId);
    List<ChatRoomDto> selectBuyersByPost(MemberDto memberDto, Long postId);
    void insertWish(Long postId, String memId);
    void deleteWish(Long postId, String memId);
    String isWishExist(Long postId, String memId);
    Integer countWish(Long postId);
    List<PostDto> selectPostListFromWish(String memId);
    List<PostDto> searchPost(String loginMemId, String searchWord);
    List<PostDto> selectPostListByCategory(MemberDto memberDto, Category category);
    void updateHits(Long postId);
    List<PostDto> postListBrief(int limit, String memId);
}
