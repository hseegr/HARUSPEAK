package com.haruspeak.api.user.dto;

import org.springframework.http.ResponseCookie;

public record TokenIssueResult (
        ResponseCookie accessCookie,
        ResponseCookie refreshCookie
){}
