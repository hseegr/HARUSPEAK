package com.haruspeak.api.summary.dto.request;

import com.haruspeak.api.common.dto.BaseDiaryListRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@Schema(description = "하루 일기 목록 조회 요청")
public class SummaryListRequest extends BaseDiaryListRequest {

}
