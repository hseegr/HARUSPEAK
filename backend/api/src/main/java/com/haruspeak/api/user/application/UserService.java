package com.haruspeak.api.user.application;

import com.haruspeak.api.user.domain.repository.UserTagRepository;
import com.haruspeak.api.user.dto.UserTag;
import com.haruspeak.api.user.dto.UserTagRaw;
import com.haruspeak.api.user.dto.response.UserTagResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 정보
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserTagRepository userTagRepository;

    @Transactional(readOnly = true)
    public UserTagResponse getUserTags(int userId) {
        List<UserTag> userTags =  userTagRepository.findByUserId(userId).stream()
                .map(detail -> new UserTag(
                        detail.getUserTagId(),
                        detail.getName(),
                        detail.getTotalUsageCount()
                ))
                .toList();

        return new UserTagResponse(userTags, userTags.size());
    }

}
