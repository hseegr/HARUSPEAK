package com.haruspeak.api.today.domain.repository;

import com.haruspeak.api.today.dto.TodayMomentEntry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodayMomentRepository {
    /**
     * 오늘의 순간 일기 작성 또는 업데이트
     * @param userId 사용자 ID
     * @param date key(날짜)
     * @param entry field(생성 시각) + value(일기 정보)
     */
    void save(Integer userId, LocalDate date, TodayMomentEntry entry);

    /**
     * 오늘 작성한 모든 순간 일기 조회
     * @param userId 사용자 ID
     * @param date key(날짜)
     * @param createdAt field (생성시각)
     * @return TodayMomentEntry 순간 일기
     */
    Optional<TodayMomentEntry> find(Integer userId, LocalDate date, LocalDateTime createdAt);


    /**
     * 오늘 작성한 모든 순간 일기 조회
     * @param userId 사용자 ID
     * @param date key(날짜)
     * @return List<TodayMomentEntry> 오늘 작성한 일기 전체
     */
    List<TodayMomentEntry> findAll(Integer userId, LocalDate date);

    /**
     * 오늘의 특정 순간 일기 삭제
     * @param userId 사용자 ID
     * @param date key(날짜)
     * @param createdAt field(생성 시각) : 삭제할 field
     */
    void delete(Integer userId, LocalDate date, String createdAt);

    /**
     * 오늘 작성한 일기 개수
     * @param userId 사용자 ID
     * @param date 오늘 날짜
     * @return count
     */
    long countByUserAndDate(Integer userId, LocalDate date);
}
