package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.post.entity.MtPlace;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MtPlaceRepository extends JpaRepository<MtPlace, Long> {
    MtPlace findByPost(Post post);
    void deleteByPost(Post post);
}
