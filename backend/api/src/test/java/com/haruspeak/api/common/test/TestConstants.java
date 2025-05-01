package com.haruspeak.api.common.test;

public class TestConstants {

    private TestConstants() {}

    public static final String FILTER_TEST_API= "/test/authenticatedUser";

    public static final Integer USER_ID = 1;
    public static final String NAME = "이름";

    public static final String TOKEN_TYPE_ACCESS = "accessToken";
    public static final String ACCESS_TOKEN = "access.token";
    public static final String VALID_ACCESS_TOKEN = "valid.access.token";

    public static final String TOKEN_TYPE_REFRESH = "refreshToken";
    public static final String REFRESH_TOKEN = "refresh.token";
    public static final String VALID_REFRESH_TOKEN = "valid.refresh.token";

    public static final long ACCESS_EXP = 1000L;
    public static final long REFRESH_EXP = 2000L;
}
