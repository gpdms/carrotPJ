package com.exercise.carrotproject.domain.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessageDto {
    String message;
    List<String> imgCode;
}
