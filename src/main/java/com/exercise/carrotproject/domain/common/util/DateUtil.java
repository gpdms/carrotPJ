package com.exercise.carrotproject.domain.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtil {

    private static final int SEC = 60;
    private static final int MIN = 60;
    private static final int HOUR = 24;
    private static final int DAY = 30;
    private static final int MONTH = 12;

    /**
     * x초전, x분전, x시간 전m, x일 전, x개월전, x년전
     */

    public static String CALCULATE_TIME_POST(Timestamp ts) {
        String msg;

        long curTime = System.currentTimeMillis();
        long regTime = ts.getTime();
        long diffTime = (curTime - regTime) / 1000;

        if (diffTime < SEC) {
            msg = diffTime + "초 전";
        } else if ((diffTime /= SEC) < MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = diffTime + "년 전";
        }

        return msg;

    }


    public static String CALCULATE_TIME(Timestamp ts) {
        String msg;

        long curTime = System.currentTimeMillis();
        long regTime = ts.getTime();
        long diffTime = (curTime - regTime) / 1000;

        if (diffTime < SEC) {
//            msg = diffTime + "초 전";
            msg = new SimpleDateFormat("HH:mm").format(ts);
        } else if ((diffTime /= SEC) < MIN) {
//            msg = diffTime + "분 전";
            msg = new SimpleDateFormat("HH:mm").format(ts);
        } else if ((diffTime /= MIN) < HOUR) {
//            msg = (diffTime) + "시간 전";
            msg = new SimpleDateFormat("HH:mm").format(ts);
        } else if ((diffTime /= HOUR) < DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = diffTime + "년 전";
        }

        return msg;

    }






}
