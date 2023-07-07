package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public interface CustomPostRepository {
    long updateHideState(Long postId);

    List<SoldPostDto> findSoldPostListByMemId(String memId);
    List<Post> selectPostForBoard(String loginMemId, Loc loginMemLoc);
    List<Post> selectPostForBoardByCategory(String loginMemId, Loc loginMemLoc, Category category);
    List<Post> searchPost(String loginMemId, String searchWord);
    List<Post> postListByLimit(String memId, int limit);
}
