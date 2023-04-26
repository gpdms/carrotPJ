package com.exercise.carrotproject.domain.common.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME;
import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME_POST;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdTime;

    @UpdateTimestamp
    @Column(insertable = false)
    private Timestamp updatedTime;

    public String getCalculatedTimeForChat() {
        return CALCULATE_TIME(this.createdTime);
    }

    public String getCalculatedTimeForPost() {
        return CALCULATE_TIME_POST(this.createdTime);
    }

}
