package com.haruspeak.api.today.application;

import com.haruspeak.api.common.util.FastApiClient;
import com.haruspeak.api.today.dto.request.MomentTagCreateRequest;
import com.haruspeak.api.today.dto.response.MomentTagCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MomentTagService {

    private final FastApiClient fastApiClient;

    // [API] AI 모먼트 태그 자동생성
    @Transactional
    public MomentTagCreateResponse createMomentTag(String uri, MomentTagCreateRequest mtcr) {
        // ai 서버에 프론트 요청값 전달 후 반환 받기
        MomentTagCreateResponse response = fastApiClient.getPrediction(uri, mtcr, MomentTagCreateResponse.class);
        return response;
    }

}