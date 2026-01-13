package com.example.redisdemo.controller;

import com.example.redisdemo.dto.RedisRequest;
import com.example.redisdemo.dto.RedisResponse;
import com.example.redisdemo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/{key}")
    public ResponseEntity<RedisResponse> getValue(@PathVariable String key) {
        try {
            Object value = redisService.get(key);
            if (value != null) {
                RedisResponse response = new RedisResponse(key, value, redisService.hasKey(key), redisService.getExpire(key));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RedisResponse(key, null, false, -1, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{key}")
    public ResponseEntity<RedisResponse> setValue(@PathVariable String key, @RequestBody RedisRequest request) {
        try {
            if (request.getTimeout() > 0 && request.getTimeUnit() != null) {
                redisService.set(key, request.getValue(), request.getTimeout(), request.getTimeUnit());
            } else {
                redisService.set(key, request.getValue());
            }
            
            Object storedValue = redisService.get(key);
            long ttl = redisService.getExpire(key);
            RedisResponse response = new RedisResponse(key, storedValue, true, ttl, "Value stored successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RedisResponse(key, null, false, -1, "Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Map<String, Object>> deleteValue(@PathVariable String key) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = redisService.delete(key);
            response.put("key", key);
            response.put("deleted", deleted);
            response.put("message", deleted ? "Key deleted successfully" : "Key not found");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error deleting key: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{key}/exists")
    public ResponseEntity<Map<String, Object>> keyExists(@PathVariable String key) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exists = redisService.hasKey(key);
            response.put("key", key);
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error checking key: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{key}/expire")
    public ResponseEntity<Map<String, Object>> setExpiration(@PathVariable String key, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            long timeout = Long.parseLong(request.get("timeout").toString());
            String timeUnitStr = request.get("timeUnit").toString();
            TimeUnit timeUnit = TimeUnit.valueOf(timeUnitStr.toUpperCase());
            
            boolean success = redisService.expire(key, timeout, timeUnit);
            response.put("key", key);
            response.put("timeout", timeout);
            response.put("timeUnit", timeUnitStr);
            response.put("success", success);
            response.put("message", success ? "Expiration set successfully" : "Failed to set expiration");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error setting expiration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{key}/ttl")
    public ResponseEntity<Map<String, Object>> getTTL(@PathVariable String key) {
        Map<String, Object> response = new HashMap<>();
        try {
            long ttl = redisService.getExpire(key);
            response.put("key", key);
            response.put("ttl", ttl);
            response.put("exists", ttl != -1);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error getting TTL: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Simple health check - try to set and get a test key
            String testKey = "health:check:" + System.currentTimeMillis();
            redisService.set(testKey, "ok", 5, TimeUnit.SECONDS);
            Object value = redisService.get(testKey);
            redisService.delete(testKey);
            
            response.put("status", "healthy");
            response.put("redis", "connected");
            response.put("timestamp", System.currentTimeMillis());
            response.put("testResult", "ok".equals(value));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "unhealthy");
            response.put("redis", "disconnected");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
}
