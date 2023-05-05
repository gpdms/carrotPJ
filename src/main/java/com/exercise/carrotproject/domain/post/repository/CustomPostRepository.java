package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.Category;
import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public interface CustomPostRepository {
    long updateHideState(Long postId);
    List<SoldPostDto> getSoldList(String memId);
    List<Post> selectBoardPost(String loginMemId, Loc loginMemLoc);
    List<Post> selectBoardPostByCategory(String loginMemId, Loc loginMemLoc, Category category);
    List<Post> searchPost(String loginMemId, String searchWord);
    List<Post> postListByLimit(int limit, String memId);
    BooleanExpression notExistsBlock(String loginMemId);
    BooleanExpression locEq(Loc loc);
    BooleanExpression memIdEq(String memId);
}
