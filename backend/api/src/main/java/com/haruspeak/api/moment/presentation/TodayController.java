package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.moment.application.TodayService;
import com.haruspeak.api.moment.dto.request.MomentWriteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/today")
public class TodayController {

    private final TodayService todayService;

    @PostMapping("")
    public ResponseEntity<Void> writeMoment(@RequestBody MomentWriteRequest request, @AuthenticatedUser Integer userId){
        todayService.saveMoment(request,userId);
        return ResponseEntity.ok().build();
    }
}
