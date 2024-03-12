package org.nadarkanloev.vktest.Service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


public class UUIDGenerator {
    public static long generateUniqueId(){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        long year = now.getYear();
        long month = now.getMonthValue();
        long day = now.getDayOfMonth();
        long timeInMillis = now.toEpochSecond(ZoneOffset.UTC) * 1000;
        return year + month + day + timeInMillis;
    }
}
