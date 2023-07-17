package com.exercise.carrotproject.web.member.form.memberInfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class BlockForm {
    @NotNull
    private final String fromMemId;
    @NotNull
    private final String toMemId;

    @JsonCreator
    private BlockForm(@JsonProperty("fromMemId") String fromMemId,
                      @JsonProperty("toMemId") String toMemId) {
        this.fromMemId = fromMemId;
        this.toMemId = toMemId;
    }
}
