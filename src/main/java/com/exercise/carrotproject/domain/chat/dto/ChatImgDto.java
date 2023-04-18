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
public class ChatImgDto {
    private Long chatImgId;
    private Long chatId;
    private String imgPath;

    public ChatImgDto(Long chatImgId, Long chatId, String imgPath) {
        this.chatImgId = chatImgId;
        this.chatId = chatId;
        this.imgPath = imgPath;
    }
}
