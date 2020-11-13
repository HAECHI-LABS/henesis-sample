package io.haechi.henesis.assignment.support;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class Utils {
    public static LocalDateTime toLocalDateTime(String date) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(date)),
                TimeZone.getDefault().toZoneId()
        );
    }
}
