package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.moment.application.ActiveDailyMomentService;
import com.haruspeak.api.moment.dto.request.MomentListRequest;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import com.haruspeak.api.moment.dto.response.MomentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moment")
public class MomentController {
    private final ActiveDailyMomentService activeDailyMomentService;

    /**
     * 순간 일기 상세 조회
     * @param momentId
     * @param auth
     * @return
     */
    @GetMapping("/{momentId}")
    public MomentDetailResponse getMomentDetail(@PathVariable Integer momentId, Authentication auth){
        if (auth == null || auth.getPrincipal() == null) {
            throw new HaruspeakException(ErrorCode.USER_NOT_FOUND);
        }
        return activeDailyMomentService.findMomentDetail(momentId);
    }

    /**
     * 순간 일기 목록 조회
     * @param before
     * @param limit
     * @param startDate
     * @param endDate
     * @param userTags
     * @param auth
     * @return
     */
    @GetMapping("/")
    public MomentListResponse getMomentList(
            @RequestParam(required = false) LocalDateTime before,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) List<String> userTags,
            Authentication auth){

        if (auth == null || auth.getPrincipal() == null) {
            throw new HaruspeakException(ErrorCode.USER_NOT_FOUND);
        }

        Integer userId = (Integer) auth.getPrincipal();

        MomentListRequest request = new MomentListRequest(
                before,
                limit,
                startDate,
                endDate,
                userTags
        );
        return activeDailyMomentService.findMomentList(request,userId);
    }
}
