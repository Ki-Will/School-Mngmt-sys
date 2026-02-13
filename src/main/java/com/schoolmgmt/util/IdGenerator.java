package com.schoolmgmt.util;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DecimalFormat COUNTER_FORMAT = new DecimalFormat("000");
    private static final AtomicInteger COUNTER = new AtomicInteger();

    private IdGenerator() {
    }

    public static String newId(String prefix) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DATE_FORMATTER);
        String counterPart = COUNTER_FORMAT.format(COUNTER.updateAndGet(value -> (value + 1) % 1000));
        return prefix + "-" + timestamp + counterPart;
    }
}
