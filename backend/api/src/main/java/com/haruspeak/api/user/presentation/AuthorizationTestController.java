package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/test")
public class AuthorizationTestController {

    /**
     * 인증된 유저의 ID를 어노테이션을 통해 꺼내서 확인하는 테스트 API
     */
    @GetMapping("/authenticatedUser")
    public ResponseEntity<Integer> refreshAccessToken(@AuthenticatedUser Integer userId) {
        return ResponseEntity.ok(userId);
    }
}