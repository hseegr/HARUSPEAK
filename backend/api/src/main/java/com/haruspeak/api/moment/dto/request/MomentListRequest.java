package com.haruspeak.api.moment.dto.request;

import com.haruspeak.api.common.dto.BaseDiaryListRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString(callSuper = true)
@Schema(description = "순간 일기 목록 조회 요청")
public class MomentListRequest extends BaseDiaryListRequest {
        @Schema(description = "사용자 태그 ID 목록", nullable = true, example = "1,2,3")
        @Size(max = 10, message = "태그는 최대 10개까지 선택할 수 있습니다.")
        private List<Integer> userTags;
}