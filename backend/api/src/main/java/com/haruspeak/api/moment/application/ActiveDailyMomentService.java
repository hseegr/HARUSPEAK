package com.haruspeak.api.moment.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.moment.domain.repository.ActiveDailyMomentRepository;
import com.haruspeak.api.moment.dto.MomentDetailRaw;
import com.haruspeak.api.moment.dto.response.MomentDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActiveDailyMomentService {
    private final ActiveDailyMomentRepository activeDailyMomentRepository;

    @Transactional(readOnly = true)
    public MomentDetailResponse findMomentDetail(Integer momentId){
        MomentDetailRaw raw = activeDailyMomentRepository.getMomentDetailRaw(momentId)
                .orElseThrow(() -> new HaruspeakException(ErrorCode.MOMENT_NOT_FOUND));

        return new MomentDetailResponse(
                raw.momentId(),
                raw.momentTime(),
                splitString(raw.imageUrls()),
                raw.content(),
                splitString(raw.tags())
        );
    }

    private List<String> splitString(String source) {
        if (source == null || source.isBlank()) {
            return Collections.emptyList();
        }
        return List.of(source.split(","));
    }
}
