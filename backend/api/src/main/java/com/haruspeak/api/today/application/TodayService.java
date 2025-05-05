//package com.haruspeak.api.today.application;
//
//import com.haruspeak.api.today.domain.repository.TodayMomentRedisRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class TodayService {
//
//    private final TodayMomentRedisRepository todayRedisRepository;
//
//    /**
//     * Date 날짜에 작성한 일기 개수
//     * @param userId 사용자 ID
//     * @param date 날짜
//     * @return count
//     */
//    public int getTodayMomentCount(int userId, LocalDate date) {
//        return todayRedisRepository.countByUserAndDate(userId, date);
//    }
//
//}
