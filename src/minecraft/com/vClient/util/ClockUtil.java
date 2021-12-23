package com.vClient.util;

public class ClockUtil {
    private long current, last;
    public void updateTime() {
        current = System.nanoTime() / 1000000L;
    }
    public void resetTime() {
        last = current = System.nanoTime() / 1000000L;
    }
    public long elapsedTime() {
        return current - last;
    }
}
