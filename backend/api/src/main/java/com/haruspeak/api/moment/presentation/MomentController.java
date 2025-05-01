package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.moment.application.ActiveDailyMomentService;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.haruspeak.api.moment.dto.response.MomentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moment")
public class MomentController {
    private final ActiveDailyMomentService activeDailyMomentService;

    /**
     * 순간 일기 상세 조회
     * @param momentId
     * @param userId
     * @return
     */
    @GetMapping("/{momentId}")
    public ResponseEntity<MomentDetailResponse> getMomentDetail(@PathVariable Integer momentId, @AuthenticatedUser Integer userId) {
        return ResponseEntity.ok(activeDailyMomentService.getMomentDetail(userId, momentId));
    }

    /**
     * 순간 일기 목록 조회
     * @param before
     * @param limit
     * @param startDate
     * @param endDate
     * @param userTags
     * @param userId
     * @return
     */
    @GetMapping("")
    public ResponseEntity<MomentListResponse> getMomentList(
            @RequestParam(required = false) LocalDateTime before,
            @RequestParam(required = false, defaultValue = "30") Integer limit,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) List<Integer> userTags,
            @AuthenticatedUser Integer userId){
        MomentListRequest request = new MomentListRequest(
                before,
                limit + 1,
                startDate,
                endDate,
                userTags
        );
        return ResponseEntity.ok(activeDailyMomentService.getMomentList(request,userId));
    }
}
