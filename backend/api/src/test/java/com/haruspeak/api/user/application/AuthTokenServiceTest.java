package com.haruspeak.api.user.application;

import com.haruspeak.api.common.exception.user.UnauthorizedException;
import com.haruspeak.api.common.security.JwtTokenProvider;
import com.haruspeak.api.user.domain.repository.RefreshTokenRepositoryImpl;
import com.haruspeak.api.user.dto.TokenIssueResult;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static com.haruspeak.api.common.test.TestConstants.*;

/**
 * AuthTokenService 테스트 완료
 *
 * 🧪 토큰 발급:
 * - 성공
 *
 * 🧪 토큰 재발급:
 * - 성공
 * - 예외 ) 쿠키 : null
 * - 예외 ) 리프레시 토큰 : null
 *
 * 🧪 로그아웃:
 * - 성공 ) 정상 토큰
 * - 성공 ) 쿠키 : null
 * - 성공 ) 리프레시 토큰 : null
 */
@ExtendWith(MockitoExtension.class)
class AuthTokenServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepositoryImpl refreshTokenRepository;

    @InjectMocks
    private AuthTokenService authTokenService;

    @Nested
    @DisplayName("🧪 토큰 발급 테스트")
    class IssueTokenTest{
        /**
         * [✅ 성공 테스트] 토큰 발급
         *
         * 목적:
         * - accessToken과 refreshToken을 발급하고 쿠키로 생성하는지 확인
         *
         * 입력:
         * - userId: 사용자 ID
         * - name: 사용자 이름
         *
         * 예상 결과:
         * - accessCookie, refreshCookie 가 null이 아님
         */
        @Test
        @DisplayName("✅ 토큰 발급 성공")
        void issueToken_success() {
            when(jwtTokenProvider.issueAccessToken(USER_ID, NAME)).thenReturn(ACCESS_TOKEN);
            when(jwtTokenProvider.issueRefreshToken(USER_ID)).thenReturn(REFRESH_TOKEN);
            when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(ACCESS_EXP);
            when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(REFRESH_EXP);

            TokenIssueResult result = authTokenService.issueToken(USER_ID, NAME);

            assertThat(result.accessCookie()).isNotNull();
            assertThat(result.refreshCookie()).isNotNull();
            verify(refreshTokenRepository).saveRefreshToken(USER_ID, REFRESH_TOKEN, REFRESH_EXP);
        }
    }


    @Nested
    @DisplayName("🧪 토큰 재발급 테스트")
    class ReissueTokenTest {
        /**
         * [✅ 성공 테스트] refreshToken 기반 accessToken 재발급
         * <p>
         * 목적:
         * - 쿠키에서 refreshToken 추출 후 accessToken, refreshToken 재발급
         * <p>
         * 입력:
         * - Cookie 배열에 refreshToken 포함
         * <p>
         * 예상 결과:
         * - 토큰 재발급 성공
         */
        @Test
        @DisplayName("✅ refreshToken 기반 토큰 재발급 성공")
        void reissueToken_success() {
            Cookie[] cookies = {new Cookie(TOKEN_TYPE_REFRESH, VALID_REFRESH_TOKEN)};
            when(jwtTokenProvider.getUserIdFromToken(VALID_REFRESH_TOKEN)).thenReturn(USER_ID);
            when(jwtTokenProvider.getNameFromToken(VALID_REFRESH_TOKEN)).thenReturn(NAME);
            when(jwtTokenProvider.issueAccessToken(anyInt(), anyString())).thenReturn(ACCESS_TOKEN);
            when(jwtTokenProvider.issueRefreshToken(anyInt())).thenReturn(REFRESH_TOKEN);
            when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(ACCESS_EXP);
            when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(REFRESH_EXP);

            TokenIssueResult result = authTokenService.reissueTokens(cookies);

            assertThat(result.accessCookie()).isNotNull();
            assertThat(result.refreshCookie()).isNotNull();

            verify(refreshTokenRepository).saveRefreshToken(USER_ID, REFRESH_TOKEN, REFRESH_EXP);
        }

        /**
         * [⚠️ 예외 테스트] 쿠키 없음 → InvalidTokenException 발생
         * <p>
         * 목적:
         * - 쿠키가 없으면 예외 발생
         * <p>
         * 입력:
         * - cookie null
         * <p>
         * 예상 결과:
         * - UnauthorizedException 예외 발생
         */
        @Test
        @DisplayName("⚠️ 쿠키 없음 → UnauthorizedException")
        void reissueToken_cookieNull_throwsInvalidTokenException() {
            assertThatThrownBy(() -> authTokenService.reissueTokens(new Cookie[]{}))
                    .isInstanceOf(UnauthorizedException.class);
        }

        /**
         * [⚠️ 예외 테스트] 쿠키에 refreshToken 없음 → UnauthorizedException 발생
         * <p>
         * 목적:
         * - 쿠키는 null이 아니나, refreshToken이 없으면 예외 발생
         * <p>
         * 입력:
         * - refreshToken 없는 쿠키 배열
         * <p>
         * 예상 결과:
         * - UnauthorizedException 예외 발생
         */
        @Test
        @DisplayName("⚠️ refreshToken 없음 → UnauthorizedException")
        void reissueToken_refreshTokenNull_throwsInvalidTokenException() {
            assertThatThrownBy(() -> authTokenService.reissueTokens(new Cookie[]{}))
                    .isInstanceOf(UnauthorizedException.class);
        }
    }



    @Nested
    @DisplayName("🧪 로그아웃 테스트")
    class LogoutTokenTest {
        /**
         * [✅ 성공 테스트] 로그아웃 시 refreshToken 삭제 + 만료 쿠키 반환
         *
         * 목적:
         * - Redis에서 refreshToken 삭제 후 만료된 쿠키 반환
         *
         * 입력:
         * - 유효한 refreshToken 쿠키
         *
         * 예상 결과:
         * - Redis 삭제 실행 + 만료 쿠키 반환
         */
        @Test
        @DisplayName("✅ 로그아웃 처리 성공")
        void expireToken_success() {
            Cookie[] cookies = { new Cookie(TOKEN_TYPE_REFRESH, REFRESH_TOKEN) };

            when(jwtTokenProvider.getUserIdFromToken(REFRESH_TOKEN)).thenReturn(USER_ID);

            TokenIssueResult result = authTokenService.expireToken(cookies);

            verify(refreshTokenRepository).deleteRefreshToken(USER_ID);
            assertThat(result.accessCookie().getMaxAge().getSeconds()).isEqualTo(0);
            assertThat(result.refreshCookie().getMaxAge().getSeconds()).isEqualTo(0);
        }

        /**
         * [✅ 성공 테스트] 쿠키가 null일 때 로그아웃 정상 반환 되는지 검증
         *
         * 목적:
         * - cookie가 없는 경우에도 만료된 토큰으로 반환
         *
         * 입력:
         * - cookie null
         *
         * 예상 결과:
         * - 만료 쿠키 반환
         */
        @Test
        @DisplayName("✅ 쿠키 없음 -> 로그아웃 처리 성공")
        void expireToken_cookieNull_success() {
            TokenIssueResult result = authTokenService.expireToken(null);

            assertThat(result.accessCookie().getMaxAge().getSeconds()).isEqualTo(0);
            assertThat(result.refreshCookie().getMaxAge().getSeconds()).isEqualTo(0);
        }

        /**
         * [✅ 성공 테스트] refreshToken null일 때 로그아웃 정상 반환 되는지 검증
         *
         * 목적:
         * - cookie 배일은 있고 refreshToken이 없는 경우에도 만료된 토큰으로 반환
         *
         * 입력:
         * - 빈 쿠키 배열
         *
         * 예상 결과:
         * - 만료 쿠키 반환
         */
        @Test
        @DisplayName("✅ 리프레시 토큰 없음 -> 로그아웃 처리 성공")
        void expireToken_refreshTokenNull_success() {
            TokenIssueResult result = authTokenService.expireToken(new Cookie[]{});

            assertThat(result.accessCookie().getMaxAge().getSeconds()).isEqualTo(0);
            assertThat(result.refreshCookie().getMaxAge().getSeconds()).isEqualTo(0);
        }
    }
}
