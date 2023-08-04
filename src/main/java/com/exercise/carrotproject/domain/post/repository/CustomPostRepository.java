package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPostRepository {
//    long updateHideState(Long postId);
    List<SoldPostDto> getSoldPostListByMemId(String memId);

    Page<PostDto> getOnSalePostPageByMemId(String memId, Pageable pageable);
    Page<SoldPostDto> getSoldPostPageByMemId(String memId, Pageable pageable);
    Page<PostDto> getHiddenPostPageByMemId(String memId, Pageable pageable);

    List<Post> selectPostForBoard(String loginMemId, Loc loginMemLoc);
    List<Post> selectPostForBoardByCategory(String loginMemId, Loc loginMemLoc, Category category);
    List<Post> searchPost(String loginMemId, String searchWord);

    List<Post> getMyPostListByLimit(String memId, int limit);
    List<Post> getPostListByLimit(String loginMemId, Loc loginMemLoc, int limit);
}
