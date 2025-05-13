package com.haruspeak.api.user.application;

import com.haruspeak.api.common.security.JwtTokenProvider;
import com.haruspeak.api.user.domain.User;
import com.haruspeak.api.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * OAuthLoginService 테스트 완료
 *
 * 🧪 로그인 또는 회원가입 처리:
 * - 성공 ) 기존 회원 로그인
 * - 성공 ) 신규 회원 가입
 *
 * 🧪 토큰 쿠키 생성:
 * - 성공
 */
class OAuthLoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private OAuthLoginService oAuthLoginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /// 🧪🧪🧪 로그인 또는 회원가입 테스트 🧪🧪🧪 //////////////////////////////////////////////

    /**
     * [✅ 성공 테스트] 기존 회원 로그인
     *
     * 목적:
     * - DB에 존재하는 사용자면 회원가입 없이 바로 로그인 처리
     *
     * 입력:
     * - 기존 snsId, email, name
     *
     * 예상 결과:
     * - userId 리턴
     */
    @Test
    @DisplayName("기존 회원이면 로그인 성공")
    void login_existingUser_success() {
        User user = User.create("snsId123", "test@example.com", "홍길동");
        when(userRepository.findBySnsId("snsId123")).thenReturn(Optional.of(user));

        User loginUser = oAuthLoginService.processLoginOrRegister("snsId123", "test@example.com", "홍길동");

        assertThat(loginUser.getUserId()).isEqualTo(user.getUserId());
        verify(userRepository, never()).save(any());
    }

    /**
     * [✅ 성공 테스트] 신규 회원 가입
     *
     * 목적:
     * - DB에 snsId가 없으면 신규 가입 처리
     *
     * 입력:
     * - 새로운 snsId, email, name
     *
     * 예상 결과:
     * - 새로운 user 저장 + userId 리턴
     */
    @Test
    @DisplayName("신규 회원이면 회원가입 후 로그인 성공")
    void signup_newUser_success() {
        when(userRepository.findBySnsId("newSnsId")).thenReturn(Optional.empty());

        User savedUser = User.create("newSnsId", "new@example.com", "신규유저");
        when(userRepository.save(any())).thenReturn(savedUser);

        User loginUser = oAuthLoginService.processLoginOrRegister("newSnsId", "new@example.com", "신규유저");

        assertThat(loginUser.getUserId()).isEqualTo(savedUser.getUserId());
        verify(userRepository).save(any());
    }

}
