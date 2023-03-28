package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@AllArgsConstructor
@Getter
@Builder
@ToString
public class BlockDto {
    private Long blockId;
    private MemberDto fromMem;
    private MemberDto toMem;
}
