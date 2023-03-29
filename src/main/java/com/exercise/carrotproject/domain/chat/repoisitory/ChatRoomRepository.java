package com.exercise.carrotproject.domain.chat.repoisitory;

import com.exercise.carrotproject.domain.chat.entity.ChatRoom;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByPostAndSellerAndBuyer(Post post, Member seller, Member buyer);
    List<ChatRoom> findBySellerOrBuyer(Member seller, Member buyer);
}
