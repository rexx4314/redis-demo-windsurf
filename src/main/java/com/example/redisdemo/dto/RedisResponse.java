package com.example.redisdemo.dto;

public class RedisResponse {
    private String key;
    private Object value;
    private boolean exists;
    private long ttl;
    private String message;

    public RedisResponse() {}

    public RedisResponse(String key, Object value, boolean exists, long ttl) {
        this.key = key;
        this.value = value;
        this.exists = exists;
        this.ttl = ttl;
    }

    public RedisResponse(String key, Object value, boolean exists, long ttl, String message) {
        this.key = key;
        this.value = value;
        this.exists = exists;
        this.ttl = ttl;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
