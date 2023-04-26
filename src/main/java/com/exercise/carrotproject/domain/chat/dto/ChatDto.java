package com.exercise.carrotproject.domain.chat.dto;

import com.exercise.carrotproject.domain.chat.entity.ChatImg;
import com.exercise.carrotproject.domain.enumList.ImgState;
import com.exercise.carrotproject.domain.enumList.ReadState;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME;

@ToString
@Data
public class ChatDto {
    private Long chatId;
    private Long roomId;
    private Long postId;
    private String fromId;
    private String toId;
    private ReadState readState;
    private String message;
    private ImgState imgState;
//    private List<ChatImgDto> chatImgList;
//    private Long chatImgId
    private List<Long> chatImgIdList;
    private Timestamp createdTime;

    public ChatDto(Long chatId, Long roomId, Long postId, String fromId, String toId, ReadState readState, String message, ImgState imgState, List<Long> chatImgIdList, Timestamp createdTime) {
        this.chatId = chatId;
        this.roomId = roomId;
        this.postId = postId;
        this.fromId = fromId;
        this.toId = toId;
        this.readState = readState;
        this.message = message;
        this.imgState = imgState;
//        this.chatImgList = chatImgList;
//        this.chatImgId = chatImgId;
        this.chatImgIdList = chatImgIdList;
        this.createdTime = createdTime;
    }

    public String getCalculatedTimeForChat() {
        return CALCULATE_TIME(this.createdTime);
    }
}
