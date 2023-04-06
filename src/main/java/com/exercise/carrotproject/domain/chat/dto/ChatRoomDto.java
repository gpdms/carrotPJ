package com.exercise.carrotproject.domain.chat.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME;

@ToString
@Data
public class ChatRoomDto {
    private Long roomId;
    private String sellerId;
    private String buyerId;
    private String message;
    private String createdTime;
    private Long unacknowledgedMessageCount;

    public ChatRoomDto(Long roomId, String sellerId, String buyerId, String message, Timestamp createdTime, Long unacknowledgedMessageCount) {
        this.roomId = roomId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.message = message;
        this.createdTime = CALCULATE_TIME(createdTime);
        this.unacknowledgedMessageCount = unacknowledgedMessageCount;
    }
}
