package com.exercise.carrotproject.domain.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberJdbcRepositoryTest {

    @Test
    void time() {
        // LocalDateTime 객체 생성
        LocalDateTime t= LocalDateTime.now();
        // LocalDateTime -> Instant -> Date 변환
        java.util.Date date = Date.from(t.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("date = " + date);
        Timestamp timestamp = Timestamp.valueOf(t);
        System.out.println("timestamp = " + timestamp);
        java.sql.Date date1 = new Date(date.getTime());
        System.out.println("date1 = " + date1);
    }
}