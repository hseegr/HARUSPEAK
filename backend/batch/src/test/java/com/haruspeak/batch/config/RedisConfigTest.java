package com.haruspeak.batch.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    @Qualifier("batchRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @Qualifier("apiRedisTemplate")
    private RedisTemplate<String, Object> apiRedisTemplate;

    @Test
    public void testRedisTemplates() {
        assertNotNull(redisTemplate);
        assertNotNull(apiRedisTemplate);
    }
}
