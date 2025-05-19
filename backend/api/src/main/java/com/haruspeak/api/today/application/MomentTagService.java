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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        // 요청값 : 기존 태그, 컨텐츠 내용
        List<String> prevTagList = mtcr.tags();
        if(hasDuplicate(prevTagList)) throw new HaruspeakException(ErrorCode.DUPLICATE_TAG_VALUE); // prevTagList 에 중복값이 있으면
        String content = mtcr.content(); // 요청 온 컨텐츠 내용

        // 1. 빈컨텐츠가 요청으로 전달될 때 - 기존태그
        // 컨텐츠가 비었을 때(공백만 있는 경우도 포함) -> 태그 자동생성을 위해 컨텐츠의 내용을 입력하라는 에러
        if (content.isBlank()) throw new HaruspeakException(ErrorCode.NO_CONTENTS_AT_TAG_GENERATION);

        // 2. 요청값이 특수문자로만 이루어진 경우 - 기존태그 + "아무말"
        if (content.matches("^[^a-zA-Z0-9ㄱ-ㅎ가-힣]+$")) {
            List<String> specialCharacterList = new ArrayList<>(); // 초기화
            specialCharacterList.add("아무말"); // 아무말 담기
            List<String> specialCharacterArrayMergedTagList = createNonDuplicateMergedTagList(prevTagList, specialCharacterList); // 기존 리스트와 합치기

            // 수정페이지면 레디스 저장
            if(mtcr.isEditPage() == null) throw new HaruspeakException(ErrorCode.NO_IS_EDIT_PAGE_CONDITION); // 필드가 없을 경우(null) 에러
            if(mtcr.isEditPage()) updateTagListInRedis(mtcr, specialCharacterArrayMergedTagList, userId); // 레디스 저장

            return new MomentTagCreateResponse(specialCharacterArrayMergedTagList); // 반환
        }

        // 3. FastApi 요청해서 추천받아오는 경우 - 기존태그 + "아무말"
        List<String> recommendedTags = getAiTagResponse(uri, mtcr).recommendTags(); // ai 서버에 프론트 요청값 전달 후 반환 받기
        List<String> mergedTagList = createNonDuplicateMergedTagList(prevTagList, recommendedTags); // 리스트 합치기
        updateTagListInRedis(mtcr, mergedTagList, userId); // 레디스 업데이트

        // 수정페이지면 레디스 저장
        if(mtcr.isEditPage() == null) throw new HaruspeakException(ErrorCode.NO_IS_EDIT_PAGE_CONDITION); // 필드가 없을 경우(null) 에러
        if(mtcr.isEditPage()) updateTagListInRedis(mtcr, mergedTagList, userId); // // 레디스 저장

        if(mergedTagList.isEmpty()) throw new HaruspeakException(ErrorCode.RECOMMEND_TAGS_ARRAY_EMPTY); // 빈배열 반환시 에러처리
        return new MomentTagCreateResponse(mergedTagList); // 반환
    }

    // 두 리스트 중복 제거해서 합치기
    private List<String> createNonDuplicateMergedTagList(
            List<String> prevTagList,
            List<String> recommendedTagList
    ) {
        return Stream.concat(prevTagList.stream(), recommendedTagList.stream())
                .distinct()
                .toList();
    }

    // 결과 추천태그리스트 레디스 저장로직
    private void updateTagListInRedis(MomentTagCreateRequest mtcr, List<String> mergedTagList, Integer userId) {
        TagUpdateRequest tagUpdateRequest = new TagUpdateRequest(mtcr.createdAt(), mergedTagList);
        todayService.updateTag(tagUpdateRequest, userId);
    }

    // FastApi 요청보낸 결과 받기
    private MomentTagCreateResponse getAiTagResponse(String uri, MomentTagCreateRequest mtcr) {
        return fastApiClient.getPrediction(uri, mtcr, MomentTagCreateResponse.class);
    }

    private boolean hasDuplicate(List<String> tagList) {
        Set<String> unique = new HashSet<>();
        for (String tag : tagList) {
            if (!unique.add(tag)) return true; // 중복 있으면
        }
        return false; // 중복 없음
    }
}