package com.haruspeak.api.common.exception.user;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class UserRegisterException extends HaruspeakException {
    public UserRegisterException() {
        super(ErrorCode.USER_REGISTER_ERROR);
    }
}