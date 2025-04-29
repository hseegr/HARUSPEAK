package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.moment.application.ActiveDailyMomentService;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moment")
public class MomentController {
    private final ActiveDailyMomentService activeDailyMomentService;

    @GetMapping("/{momentId}")
    public MomentDetailResponse getMomentDetail(@PathVariable Integer momentId, Authentication auth){
        if (auth == null || auth.getPrincipal() == null) {
            throw new HaruspeakException(ErrorCode.USER_NOT_FOUND); // 또는 커스텀 예외 던지기
        }
        return activeDailyMomentService.findMomentDetail(momentId);
    }
}
