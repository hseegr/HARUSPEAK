package com.haruspeak.api.user.application;

import com.haruspeak.api.user.domain.User;
import com.haruspeak.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2 로그인 성공 후 사용자 조회 또는 가입 서비스
 */
@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UserRepository userRepository;

    /**
     * 사용자 조회 또는 신규 가입 처리
     *
     * @param snsId 구글 sub
     * @param email 이메일
     * @param name 이름
     * @return 사용자 ID
     */
    @Transactional
    public Integer processLoginOrRegister(String snsId, String email, String name) {
        return userRepository.findBySnsId(snsId)
                .orElseGet(() -> userRepository.save(User.create(snsId, email, name)))
                .getUserId();
    }
}
