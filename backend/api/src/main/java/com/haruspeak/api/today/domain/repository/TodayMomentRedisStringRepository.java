package com.haruspeak.api.today.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haruspeak.api.today.domain.TodayMoment;
import com.haruspeak.api.today.dto.TodayMomentEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 사용자의 날짜별 오늘의 일기 저장소
 * - key: "user:{userId}:moment:{date}"
 * - field: createdAt : 생성 시각
 * - value: todayMoment : 순간 일기
 * - 만료 
 */
@Repository
@RequiredArgsConstructor
public class TodayMomentRedisStringRepository implements TodayMomentRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 사용자의 날짜별 일기 Key
     * @param userId 사용자 ID
     * @param date 날짜
     * @return redis key
     */
    private String redisKey(Integer userId, LocalDate date) {
        return String.format("user:%d:moment:%s", userId, date.toString());
    }

    /**
     * 오늘의 순간 일기 작성 또는 업데이트
     * @param userId 사용자 ID
     * @param date key(날짜)
     * @param entry field(생성 시각) + value(일기 정보)
     */
    @Override
    public void save(Integer userId, LocalDate date, TodayMomentEntry entry) {
        redisTemplate.opsForHash().put(
                redisKey(userId, date),
                entry.createdAt().toString(),
                serialize(entry.moment())
        );
    }

    /**
     * 오늘 작성한 모든 순간 일기 조회
     * @param userId 사용자 ID
     * @param date key(날짜)
     * @param createdAt field (생성시각)
     * @return TodayMomentEntry 순간 일기
     */
    @Override
    public Optional<TodayMomentEntry> find(Integer userId, LocalDate date, LocalDateTime createdAt) {
        String redisKey = redisKey(userId, date);
        String fieldKey = createdAt.toString();

        Object raw = redisTemplate.opsForHash().get(redisKey, fieldKey);
        if (raw == null) {
            return Optional.empty();
        }

        TodayMoment moment = parse(raw.toString());
        return Optional.of(new TodayMomentEntry(createdAt, moment));
    }


    /**
     * 오늘 작성한 모든 순간 일기 조회
     * + 최신순 정렬
     * @param userId 사용자 ID
     * @param date key(날짜)
     * @return TodayMomentEntry 순간 일기
     */
    @Override
    public List<TodayMomentEntry> findAll(Integer userId, LocalDate date) {
        Map<Object, Object> all = redisTemplate.opsForHash().entries(redisKey(userId, date));
        return all.entrySet().stream()
                .map(entry -> {
                    LocalDateTime createdAt = LocalDateTime.parse((String) entry.getKey());
                    TodayMoment moment = parse(entry.getValue().toString());
                    return new TodayMomentEntry(createdAt, moment);
                })
                .sorted((a, b) -> b.moment().momentTime().compareTo(a.moment().momentTime())) // momentTime 기준 역순 정렬
                .toList();
    }


    /**
     * 오늘의 특정 순간 일기 삭제
     * @param userId 사용자 ID
     * @param date key(날짜)
     * @param createdAt field(생성 시각) : 삭제할 field
     */
    @Override
    public void delete(Integer userId, LocalDate date, String createdAt) {
        redisTemplate.opsForHash().delete(redisKey(userId, date), createdAt);
    }

    /**
     * 오늘 작성한 일기 개수
     * @param userId 사용자 ID
     * @param date 오늘 날짜
     * @return count
     */
    @Override
    public long countByUserAndDate(Integer userId, LocalDate date) {
        return redisTemplate.opsForHash().size(redisKey(userId, date));
    }

    /**
     * TodayMoment -> JSON 문자열로 직렬화
     */
    private String serialize(TodayMoment moment) {
        try {
            return objectMapper.writeValueAsString(moment);
        } catch (Exception e) {
            throw new RuntimeException("Redis 직렬화 실패", e);
        }
    }

    /**
     * JSON 문자열 -> TodayMoment 객체로 역직렬화
     */
    private TodayMoment parse(String json) {
        try {
            return objectMapper.readValue(json, TodayMoment.class);
        } catch (Exception e) {
            throw new RuntimeException("Redis 역직렬화 실패", e);
        }
    }
}
