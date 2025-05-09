package com.haruspeak.api.summary.domain;

import lombok.Getter;

@Getter
public enum ThumbnailRegenState {
    QUEUED, GENERATING, PENDING, RETRYING, FAILED, SUCCESS
}