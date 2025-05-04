package com.haruspeak.api.user.application;

import com.haruspeak.api.common.exception.user.UserRegisterException;
import com.haruspeak.api.user.domain.User;
import com.haruspeak.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2 로그인 성공 후 사용자 조회 또는 가입 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
    public User processLoginOrRegister(String snsId, String email, String name) {
        return userRepository.findBySnsId(snsId)
                .map(user -> {
                    log.debug("✅ 기존 사용자 로그인");
                    return user;
                })
                .orElseGet(() -> {
                    try {
                        log.info("🎉 신규 사용자 회원가입 (snsId: {}, email: {})", snsId, email);
                        return userRepository.save(User.create(snsId, email, name));
                    } catch (Exception e) {
                        log.error("❌ 회원가입 실패 (snsId: {}, email: {})", snsId, email, e);
                        throw new UserRegisterException();
                    }
                })
                ;

    }
}
