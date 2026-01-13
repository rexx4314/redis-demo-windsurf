package com.example.redisdemo;

import com.example.redisdemo.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisEmbeddedIntegrationTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private LettuceConnectionFactory connectionFactory;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", () -> "localhost");
        registry.add("spring.redis.port", () -> "6379");
        registry.add("spring.redis.timeout", () -> "2000ms");
    }

    @BeforeEach
    void setUp() {
        try {
            redisService.delete("embedded:test:key");
            redisService.delete("embedded:test:expire");
            redisService.delete("embedded:test:object");
        } catch (Exception e) {
            // Redis might not be available, skip cleanup
        }
    }

    @Test
    void testRedisConnection() {
        try {
            assertEquals("PONG", connectionFactory.getConnection().ping());
        } catch (Exception e) {
            // Skip test if Redis is not available
            org.junit.jupiter.api.Assumptions.assumeTrue(false, "Redis is not available: " + e.getMessage());
        }
    }

    @Test
    void testSetAndGet() {
        org.junit.jupiter.api.Assumptions.assumeTrue(() -> {
            try {
                return "PONG".equals(connectionFactory.getConnection().ping());
            } catch (Exception e) {
                return false;
            }
        }, "Redis is not available");

        String key = "embedded:test:key";
        String value = "embedded-test-value";
        
        redisService.set(key, value);
        Object retrieved = redisService.get(key);
        
        assertEquals(value, retrieved);
    }

    @Test
    void testSetWithExpiration() {
        org.junit.jupiter.api.Assumptions.assumeTrue(() -> {
            try {
                return "PONG".equals(connectionFactory.getConnection().ping());
            } catch (Exception e) {
                return false;
            }
        }, "Redis is not available");

        String key = "embedded:test:expire";
        String value = "expire-value";
        
        redisService.set(key, value, 1, TimeUnit.SECONDS);
        
        assertTrue(redisService.hasKey(key));
        
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertFalse(redisService.hasKey(key));
    }

    @Test
    void testDelete() {
        org.junit.jupiter.api.Assumptions.assumeTrue(() -> {
            try {
                return "PONG".equals(connectionFactory.getConnection().ping());
            } catch (Exception e) {
                return false;
            }
        }, "Redis is not available");

        String key = "embedded:test:key";
        String value = "to-be-deleted";
        
        redisService.set(key, value);
        assertTrue(redisService.hasKey(key));
        
        boolean deleted = redisService.delete(key);
        assertTrue(deleted);
        assertFalse(redisService.hasKey(key));
    }

    @Test
    void testHasKey() {
        org.junit.jupiter.api.Assumptions.assumeTrue(() -> {
            try {
                return "PONG".equals(connectionFactory.getConnection().ping());
            } catch (Exception e) {
                return false;
            }
        }, "Redis is not available");

        String key = "embedded:test:key";
        
        assertFalse(redisService.hasKey(key));
        
        redisService.set(key, "value");
        assertTrue(redisService.hasKey(key));
    }

    @Test
    void testComplexObject() {
        org.junit.jupiter.api.Assumptions.assumeTrue(() -> {
            try {
                return "PONG".equals(connectionFactory.getConnection().ping());
            } catch (Exception e) {
                return false;
            }
        }, "Redis is not available");

        String key = "embedded:test:object";
        TestObject obj = new TestObject("embedded-name", 35);
        
        redisService.set(key, obj);
        Object retrieved = redisService.get(key);
        
        assertNotNull(retrieved);
        assertTrue(retrieved instanceof TestObject);
        TestObject retrievedObj = (TestObject) retrieved;
        assertEquals(obj.getName(), retrievedObj.getName());
        assertEquals(obj.getAge(), retrievedObj.getAge());
    }

    public static class TestObject {
        private String name;
        private int age;

        public TestObject() {}

        public TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
