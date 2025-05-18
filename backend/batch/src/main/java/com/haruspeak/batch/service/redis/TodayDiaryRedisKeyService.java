package com.haruspeak.batch.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class TodayDiaryRedisKeyService {
    private final RedisTemplate<String, String> redisTemplate;

    public TodayDiaryRedisKeyService(@Qualifier("processingKeyRedisTemplate") RedisTemplate<String, String>  redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String TODAY_DIARY_KEYS = "todayDiary:keys";
    private static final String TODAY_DIARY_PROCESSING = "todayDiary:processing";

    public void pushAllKeys(Set<String> keys){
        if (keys == null || keys.isEmpty()) return;
        redisTemplate.opsForSet().add(TODAY_DIARY_KEYS, keys.toArray(new String[0]));
    }

    public String popOneKey(){
        return redisTemplate.opsForSet().pop(TODAY_DIARY_KEYS);
    }

    public void pushProcessingKey(String key){
        redisTemplate.opsForSet().add(TODAY_DIARY_PROCESSING, key);
    }

    public void clearProcessingKeys(){
        boolean deleted = redisTemplate.delete(TODAY_DIARY_PROCESSING);
        if (Boolean.TRUE.equals(deleted)) {
            log.debug("✅ 처리 중 key 리스트 삭제 성공");
        } else {
            log.warn("⚠️ 처리 중 key 리스트가 없거나 삭제 실패");
        }
    }

    public void recoverProcessingKeys() {
        Set<String> processingKeys = redisTemplate.opsForSet().members(TODAY_DIARY_PROCESSING) ;
        if (processingKeys != null && !processingKeys.isEmpty()) {
            pushAllKeys(processingKeys);
            redisTemplate.delete(TODAY_DIARY_PROCESSING);
            log.debug("✅ {}개의 키 복구 완료", processingKeys.size());
        } else {
            log.warn("⚠️ 복구할 키가 없습니다");
        }
    }
}
