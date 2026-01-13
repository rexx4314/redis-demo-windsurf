package com.example.redisdemo;

import com.example.redisdemo.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "REDIS_LOCALHOST_TEST", matches = "true")
class RedisLocalhostIntegrationTest {

    @Autowired
    private RedisService redisService;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", () -> "localhost");
        registry.add("spring.redis.port", () -> "6379");
    }

    @BeforeEach
    void setUp() {
        redisService.delete("test:key");
        redisService.delete("test:expire");
    }

    @Test
    void testSetAndGet() {
        String key = "test:key";
        String value = "test-value";
        
        redisService.set(key, value);
        Object retrieved = redisService.get(key);
        
        assertEquals(value, retrieved);
    }

    @Test
    void testSetWithExpiration() {
        String key = "test:expire";
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
        String key = "test:key";
        String value = "to-be-deleted";
        
        redisService.set(key, value);
        assertTrue(redisService.hasKey(key));
        
        boolean deleted = redisService.delete(key);
        assertTrue(deleted);
        assertFalse(redisService.hasKey(key));
    }

    @Test
    void testHasKey() {
        String key = "test:key";
        
        assertFalse(redisService.hasKey(key));
        
        redisService.set(key, "value");
        assertTrue(redisService.hasKey(key));
    }

    @Test
    void testExpire() {
        String key = "test:expire";
        
        redisService.set(key, "value");
        assertTrue(redisService.hasKey(key));
        
        boolean result = redisService.expire(key, 2, TimeUnit.SECONDS);
        assertTrue(result);
        
        long ttl = redisService.getExpire(key);
        assertTrue(ttl > 0 && ttl <= 2);
    }

    @Test
    void testComplexObject() {
        String key = "test:object";
        TestObject obj = new TestObject("test-name", 25);
        
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
