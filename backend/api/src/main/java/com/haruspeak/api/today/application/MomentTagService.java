package com.haruspeak.api.today.application;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import com.haruspeak.api.common.util.FastApiClient;
import com.haruspeak.api.today.dto.request.MomentTagCreateRequest;
import com.haruspeak.api.today.dto.request.TagUpdateRequest;
import com.haruspeak.api.today.dto.response.MomentTagCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MomentTagService {

    private final FastApiClient fastApiClient;
    private final TodayService todayService;

    // [API] AI 모먼트 태그 자동생성
    @Transactional
    public MomentTagCreateResponse createMomentTag(Integer userId, String uri, MomentTagCreateRequest mtcr) {
        // ai 서버에 프론트 요청값 전달 후 반환 받기
        MomentTagCreateResponse response = fastApiClient.getPrediction(uri, mtcr, MomentTagCreateResponse.class);
        List<String> prevTags = mtcr.tags();
        List<String> recommendedTags = response.recommendTags();

        // 두 리스트 합치기 (중복 제거 없이 단순 병합)
        List<String> mergedTags = Stream.concat(prevTags.stream(), recommendedTags.stream())
                .collect(Collectors.toList());

        TagUpdateRequest tagUpdateRequest = new TagUpdateRequest(mtcr.createdAt(), mergedTags);

        // 수정페이지면 레디스 저장
        if(mtcr.isEditPage() == null) throw new HaruspeakException(ErrorCode.NO_IS_EDIT_PAGE_CONDITION);
        if(mtcr.isEditPage()) todayService.updateTag(tagUpdateRequest, userId);

        return new MomentTagCreateResponse(mergedTags);
    }

}