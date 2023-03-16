package com.exercise.carrotproject.member;

import com.exercise.carrotproject.enumlist.Loc;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private Loc loc;
}
