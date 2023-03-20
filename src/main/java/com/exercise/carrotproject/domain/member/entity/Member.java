package com.exercise.carrotproject.domain.member.entity;

import com.exercise.carrotproject.domain.common.Loc;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Member {
    @Id
    private String memId;
    private String memPwd;
    private String nickname;
    private String profPath;
    private Double mannerScore;
    private Loc loc;
}
