package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish,Long> {
    void deleteByPostAndMember(Post post, Member member);
    Wish findByPostAndMember(Post post, Member member);
    Wish findByMember(Member member);
}
