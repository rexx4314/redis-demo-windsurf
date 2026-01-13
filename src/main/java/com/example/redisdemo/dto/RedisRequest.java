package com.example.redisdemo.dto;

import java.util.concurrent.TimeUnit;

public class RedisRequest {
    private Object value;
    private long timeout;
    private TimeUnit timeUnit;

    public RedisRequest() {}

    public RedisRequest(Object value) {
        this.value = value;
    }

    public RedisRequest(Object value, long timeout, TimeUnit timeUnit) {
        this.value = value;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
