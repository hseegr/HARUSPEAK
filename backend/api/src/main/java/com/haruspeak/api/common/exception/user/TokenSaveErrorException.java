package com.haruspeak.api.common.exception.user;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class TokenSaveErrorException extends HaruspeakException {
    public TokenSaveErrorException() {
        super(ErrorCode.TOKEN_SAVE_ERROR);
    }
}