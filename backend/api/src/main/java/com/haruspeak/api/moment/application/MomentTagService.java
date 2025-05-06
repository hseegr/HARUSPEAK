package com.haruspeak.api.moment.application;

import com.haruspeak.api.common.util.FastApiClient;
import com.haruspeak.api.moment.dto.request.MomentTagCreateRequest;
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
    public String createMomentTag(String uri, MomentTagCreateRequest mtcr) {

        // ai 서버에 프론트 요청값 전달 후 반환 받기
        String result = fastApiClient.getPrediction(uri, mtcr);

        // 반환 받은 값과 나머지 데이터들 redis 에 저장
        // redis 에 저장되는 값들 : (key, (createdAt, tags))
//        String createdAt = mtcr.createdAt();

        // redis 저장
//        saveInRedis(momentTagResult);

        return result;
    }

}