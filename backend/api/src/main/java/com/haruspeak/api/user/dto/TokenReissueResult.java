package com.haruspeak.api.user.dto;

import org.springframework.http.ResponseCookie;

public record TokenReissueResult(ResponseCookie accessCookie, ResponseCookie refreshCookie) {

}
