package com.exercise.carrotproject.domain.chat.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME_CHAT;

@ToString
@Data
public class ChatRoomDto {
    private Long roomId;
    private Long postId;
    private String sellerId;
    private String buyerId;
    private String message;
    private String createdTime;
    private Long unacknowledgedMessageCount;

    private String sellerNickname;
    private String buyerNickname;

    public ChatRoomDto(Long roomId, Long postId, String sellerId, String buyerId, String message, Timestamp createdTime, Long unacknowledgedMessageCount) {
        this.roomId = roomId;
        this.postId = postId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.message = message;
        this.createdTime = CALCULATE_TIME_CHAT(createdTime);
        this.unacknowledgedMessageCount = unacknowledgedMessageCount;
    }
}
