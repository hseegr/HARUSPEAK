package com.haruspeak.batch.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.TimeZone;

@Configuration
public class RedisConfig {

    // BATCH Redis
    @Value("${spring.redis.batch.host}")
    private String batchHost;

    @Value("${spring.redis.batch.port}")
    private int batchPort;

    @Value("${spring.redis.batch.password}")
    private String batchPassword;

    // API Redis
    @Value("${spring.redis.api.host}")
    private String apiHost;

    @Value("${spring.redis.api.port}")
    private int apiPort;

    @Value("${spring.redis.api.password}")
    private String apiPassword;


    /////////////////////////// BATCH ////////////////////////////////////////////////

    /**
     * 배치 Redis 연결 풀 (LettuceConnectionFactory 사용)
     */
    @Bean
    public RedisConnectionFactory batchConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(batchHost, batchPort);
        factory.setPassword(batchPassword);  // 배치 Redis 인증 비밀번호 설정
        return factory;
    }

    /**
     * 배치 RedisTemplate 사용
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> batchRedisTemplate(RedisConnectionFactory batchConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(batchConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());  // 키 직렬화 설정
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));  // 값 직렬화 설정
        return template;
    }

    /////////////////////////// API //////////////////////////////////////////////////

    /**
     * API Redis 연결 풀 (LettuceConnectionFactory 사용)
     */
    @Bean
    public RedisConnectionFactory apiConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(apiHost, apiPort);
        factory.setPassword(apiPassword);  // API Redis 인증 비밀번호 설정
        return factory;
    }

    /**
     * API RedisTemplate 사용
     */
    @Bean(name = "apiRedisTemplate")
    public RedisTemplate<String, Object> apiRedisTemplate(RedisConnectionFactory apiConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(apiConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());  // 키 직렬화 설정
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));  // 값 직렬화 설정
        return template;
    }


    /**
     * ObjectMapper 설정 (JSON 직렬화/역직렬화 커스터마이징)
     */
    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 날짜를 TIMESTAMP로 저장하지 않도록 설정
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        return objectMapper;
    }
}
