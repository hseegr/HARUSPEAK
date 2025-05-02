package com.haruspeak.api.common.exception.today;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class BlankBase64Exception extends HaruspeakException {
    public BlankBase64Exception() {
        super(ErrorCode.BLANK_BASE64);
    }
}