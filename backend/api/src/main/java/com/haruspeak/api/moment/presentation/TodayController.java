package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.moment.application.TodayService;
import com.haruspeak.api.moment.dto.request.MomentUpdateRequest;
import com.haruspeak.api.moment.dto.request.MomentWriteRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/today")
@Tag(name = "오늘 순간 일기 관련 API", description = "")
public class TodayController {

    private final TodayService todayService;

    @PostMapping("")
    public ResponseEntity<Void> writeMoment(@RequestBody MomentWriteRequest request, @AuthenticatedUser Integer userId){
        todayService.saveMoment(request,userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/time/{time}")
    public ResponseEntity<Void> modifyMoment(@PathVariable String time, @RequestBody MomentUpdateRequest request, @AuthenticatedUser Integer userId){
        todayService.updateMoment(time, request,userId);
        return ResponseEntity.ok().build();
    }
}
