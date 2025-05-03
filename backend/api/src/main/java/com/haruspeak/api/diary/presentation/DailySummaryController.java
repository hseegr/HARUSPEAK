package com.haruspeak.api.diary.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.diary.application.DailySummaryService;
import com.haruspeak.api.diary.dto.request.DailySummaryUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summary")
@Tag(name = "하루 일기 관련 API")
public class DailySummaryController {
    private final DailySummaryService dailySummaryService;

    @PatchMapping("{summaryId}")
    public ResponseEntity<Void> modifyDailySummary(@PathVariable Integer summaryId, @RequestBody @Valid DailySummaryUpdateRequest request, @AuthenticatedUser Integer userId){
        dailySummaryService.updateDailySummary(summaryId, request);
        return ResponseEntity.ok().build();
    }
}
