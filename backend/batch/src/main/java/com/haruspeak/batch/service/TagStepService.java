package com.haruspeak.batch.service;

import com.haruspeak.batch.dto.context.MomentTagContext;
import com.haruspeak.batch.dto.context.UserTagInsert;
import com.haruspeak.batch.model.repository.MomentTagRepository;
import com.haruspeak.batch.model.repository.TagRepository;
import com.haruspeak.batch.model.repository.UserTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagStepService {

    private final TagRepository tagRepository;
    private final UserTagRepository userTagRepository;
    private final MomentTagRepository momentTagRepository;

    public void insertAll(List<MomentTagContext> contexts, String date) {
        log.debug("🐛 tags, user_tags, moment_tags INSERT");

        Set<String> uniqueTags = collectUniqueTags(contexts);
        Map<Integer, Map<String, Integer>> userTagCountMap = countUserTagsByUserId(contexts);

        List<UserTagInsert> userTagInserts = flattenToUserTagInserts(userTagCountMap, date);

        tagRepository.bulkInsertTags(uniqueTags);
        userTagRepository.bulkInsertUserTags(userTagInserts);
        momentTagRepository.bulkInsertMomentTags(contexts);
    }

    /**
     * 태그 이름만 추출 (중복 제거)
     */
    private Set<String> collectUniqueTags(List<MomentTagContext> contexts) {
        return contexts.stream()
                .flatMap(context -> context.getTags().stream())
                .collect(Collectors.toSet());
    }

    /**
     * 사용자별 태그 사용 횟수 집계
     */
    private Map<Integer, Map<String, Integer>> countUserTagsByUserId(List<MomentTagContext> contexts) {
        Map<Integer, Map<String, Integer>> result = new HashMap<>();

        for (MomentTagContext context : contexts) {
            Map<String, Integer> tagMap = result.computeIfAbsent(context.getUserId(), k -> new HashMap<>());

            for (String tag : context.getTags()) {
                tagMap.put(tag, tagMap.getOrDefault(tag, 0) + 1);
            }
        }

        return result;
    }

    /**
     * Flatten해서 insert용 DTO 리스트로 변환
     */
    private List<UserTagInsert> flattenToUserTagInserts(Map<Integer, Map<String, Integer>> tagMap, String lastUsedAt) {
        List<UserTagInsert> result = new ArrayList<>();

        for (Map.Entry<Integer, Map<String, Integer>> entry : tagMap.entrySet()) {
            int userId = entry.getKey();
            for (Map.Entry<String, Integer> tagEntry : entry.getValue().entrySet()) {
                result.add(new UserTagInsert(userId, lastUsedAt, tagEntry.getKey(), tagEntry.getValue()));
            }
        }

        return result;
    }
}
