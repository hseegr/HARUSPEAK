package com.haruspeak.api.common.security;

import com.haruspeak.api.common.exception.user.InvalidJwtInputException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * JwtTokenProvider 테스트 완료
 *
 * 🧪 AccessToken 발급:
 * - 성공
 * - 예외 ) name : null
 * - 예외 ) userId : null
 *
 * 🧪 RefreshToken 발급:
 * - 성공
 * - 예외 ) userId : null
 *
 * 🧪 토큰 prefix 추가:
 * - 성공 
 * 
 * 🧪 토큰 유효성 검사:
 * - 성공
 * - 예외 ) 잘못된 토큰 
 * - 예외 ) 만료된 토큰
 * 
 */
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private final String secretKey = "dkanfj0g1r2pskWKR3TJ5DGOTD8MFWLEHahfm2smsT0zmsDPt1==";
    private final long accessTokenExpiration = 3600000; // 1시간
    private final long refreshTokenExpiration = 2592000000L; // 30일
    private final String tokenPrefix = "Bearer ";

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpiration", accessTokenExpiration);
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenExpiration", refreshTokenExpiration);
        ReflectionTestUtils.setField(jwtTokenProvider, "tokenPrefix", tokenPrefix);
        jwtTokenProvider.init();
    }


    /// 🧪🧪🧪 AccesToken 발급 테스트 🧪🧪🧪 //////////////////////////////////////////////

    /**
     * [✅ 성공 테스트] AccessToken 발급
     *
     * 목적:
     * - 유효한 userId와 name으로 JWT를 생성했을 때, 해당 정보가 claims에 정확히 포함되는지 검증
     *
     * 입력:
     * - userId: 1
     * - name: "이름"
     *
     * 예상 결과:
     * - subject == "1"
     * - name == "이름"
     * - 생성 시간은 현재 시간 이전
     * - 만료 시간은 현재 시간 이후
     */
    @Test
    @DisplayName("Access Token 생성 성공 테스트")
    void createAccessToken_success() {
        String token = jwtTokenProvider.createAccessToken(1, "이름");

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token);

        assertThat(claims.getBody().getSubject()).isEqualTo("1");
        assertThat(claims.getBody().get("userId")).isEqualTo(1);
        assertThat(claims.getBody().get("name")).isEqualTo("이름");
        assertThat(claims.getBody().getIssuedAt()).isBefore(new Date());
        assertThat(claims.getBody().getExpiration()).isAfter(new Date());
    }


    /**
     * [⚠️ 예외 테스트] name이 null인 경우 예외 발생
     *
     * 목적:
     * - 사용자 이름(name)이 누락된 경우, JWT가 생성되지 않고 예외가 발생해야 함
     *
     * 입력:
     * - userId: 1
     * - name: null
     *
     * 예상 결과:
     * - InvalidJwtInputException 발생
     */
    @Test
    @DisplayName("name이 null이면 예외 발생 테스트")
    void createAccessToken_nameNull_throwsException() {
        assertThatThrownBy(() -> jwtTokenProvider.createAccessToken(1, null))
                .isInstanceOf(InvalidJwtInputException.class)
                .hasMessage("JWT 생성에 필요한 필드가 누락되었거나 잘못되었습니다. | field: name");
    }

    /**
     * [⚠️ 예외 테스트] userId가 null인 경우 예외 발생
     *
     * 목적:
     * - 사용자 식별값(userId)이 null일 때 JWT 생성이 불가해야 함
     *
     * 입력:
     * - userId: null
     * - name: "홍길동"
     *
     * 예상 결과:
     * - InvalidJwtInputException 발생
     */
    @Test
    @DisplayName("userId가 null이면 예외 발생 테스트")
    void createAccessToken_userIdNull_throwsException() {
        assertThatThrownBy(() -> jwtTokenProvider.createAccessToken(null, "홍길동"))
                .isInstanceOf(InvalidJwtInputException.class)
                .hasMessage("JWT 생성에 필요한 필드가 누락되었거나 잘못되었습니다. | field: userId");
    }



    /// 🧪🧪🧪 RefreshToken 발급 테스트 🧪🧪🧪 //////////////////////////////////////////////

    /**
     * [✅ 성공 테스트] RefreshToken 발급
     *
     * 목적:
     * - name 없이 userId만으로 발급된 JWT가 정상 파싱되고, name 필드는 null인지 검증
     *
     * 입력:
     * - userId: 42
     *
     * 예상 결과:
     * - subject == "42"
     * - name == null
     * - 생성 시간은 현재 시간 이전
     * - 만료 시간은 현재 시간 이후
     */
    @Test
    @DisplayName("Refresh Token 생성 성공 테스트")
    void createRefreshToken_success() {
        String token = jwtTokenProvider.createRefreshToken(42);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token);

        assertThat(claims.getBody().getSubject()).isEqualTo("42");
        assertThat(claims.getBody().get("userId")).isEqualTo(42);
        assertThat(claims.getBody().get("name")).isNull();
        assertThat(claims.getBody().getIssuedAt()).isBefore(new Date());
        assertThat(claims.getBody().getExpiration()).isAfter(new Date());
    }

    /**
     * [⚠️ 예외 테스트] userId가 null인 경우 RefreshToken 생성 예외 발생
     *
     * 목적:
     * - userId가 null이면, 토큰 생성에 실패하고 예외를 던져야 함
     *
     * 입력:
     * - userId: null
     *
     * 예상 결과:
     * - InvalidJwtInputException 발생
     */
    @Test
    @DisplayName("userId가 null이면 예외 발생 테스트")
    void createRefreshToken_userIdNull_throwsException() {
        assertThatThrownBy(() -> jwtTokenProvider.createRefreshToken(null))
                .isInstanceOf(InvalidJwtInputException.class)
                .hasMessage("JWT 생성에 필요한 필드가 누락되었거나 잘못되었습니다. | field: userId");
    }



    /// 🧪🧪🧪 토큰 prefix 추가 테스트 🧪🧪🧪 //////////////////////////////////////////////

    /**
     * [✅ 성공 테스트] 토큰 prefix 처리 성공
     *
     * 목적:
     * - raw 토큰에 "Bearer " prefix를 제대로 붙이는지 검증
     *
     * 입력:
     * - 토큰 문자열: "test.jwt.token"
     *
     * 예상 결과:
     * - getTokenWithPrefix → "Bearer test.jwt.token"
     * - removePrefix → "test.jwt.token"
     */
    @Test
    @DisplayName("Prefix 추가 성공 테스트")
    void tokenPrefixTest_success() {
        String rawToken = "test.jwt.token";
        String tokenWithPrefix = jwtTokenProvider.getTokenWithPrefix(rawToken);
        assertThat(tokenWithPrefix).isEqualTo("Bearer test.jwt.token");
    }

    
    /// 🧪🧪🧪 토큰 유효성 검사 테스트 🧪🧪🧪 //////////////////////////////////////////////

    /**
     * [✅ 성공 테스트] 유효한 토큰 검증 성공
     *
     * 목적:
     * - 생성된 access token이 validateToken 호출 시 true를 반환하는지 확인
     *
     * 입력:
     * - userId: 99
     * - name: "검증유저"
     *
     * 예상 결과:
     * - validateToken() == true
     */
    @Test
    @DisplayName("유효한 토큰이면 true 반환")
    void validateToken_validToken_returnsTrue() {
        String token = jwtTokenProvider.createAccessToken(99, "검증유저");

        boolean result = jwtTokenProvider.validateToken(token);

        assertThat(result).isTrue();
    }

    /**
     * [⚠️ 실패 테스트] 변조된 토큰 검증 실패
     *
     * 목적:
     * - 잘못된 형식 혹은 위조된 JWT에 대해 validateToken이 false를 반환하는지 확인
     *
     * 입력:
     * - invalid token string
     *
     * 예상 결과:
     * - validateToken() == false
     */
    @Test
    @DisplayName("잘못된 토큰이면 false 반환")
    void validateToken_invalidToken_returnsFalse() {
        String invalidToken = "invalid.token.payload";

        boolean result = jwtTokenProvider.validateToken(invalidToken);

        assertThat(result).isFalse();
    }

    /**
     * [⚠️ 실패 테스트] 만료된 토큰 검증 실패
     *
     * 목적:
     * - 만료된 토큰이 validateToken 호출 시 false를 반환하는지 확인
     *
     * 입력:
     * - 만료시간이 현재보다 과거인 토큰
     *
     * 예상 결과:
     * - validateToken() == false
     */
    @Test
    @DisplayName("만료된 토큰이면 false 반환")
    void validateToken_expiredToken_returnsFalse() {
        Date now = new Date();
        Date expired = new Date(now.getTime() - 1000); // 이미 만료된 시간

        String expiredToken = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        boolean result = jwtTokenProvider.validateToken(expiredToken);

        assertThat(result).isFalse();
    }
}
