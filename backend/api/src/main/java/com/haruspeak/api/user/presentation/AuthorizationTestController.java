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
     * 어노테이션 테스트를 위한 api
     * @param userId
     * @return
     */
    @GetMapping("/authenticatedUser")
    public ResponseEntity<Integer> refreshAccessToken(@AuthenticatedUser Integer userId) {
        return ResponseEntity.ok(userId);
    }
}
