package com.haruspeak.api.user.application;

import com.haruspeak.api.user.domain.UserTagDetail;
import com.haruspeak.api.user.domain.repository.UserTagJpaRepository;
import com.haruspeak.api.user.domain.repository.UserTagQdslRepository;
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

    private final UserTagJpaRepository userTagJpaRepository;
    private final UserTagQdslRepository userTagQdslRepository;

    @Transactional(readOnly = true)
    public UserTagResponse getUserTags(int userId) {
        List<UserTag> userTags = userTagJpaRepository.findByUserIdOrderByScoreDesc(userId).stream()
                .map(this::toUserTag)
                .toList();

        return new UserTagResponse(userTags, userTags.size());
    }

    private UserTag toUserTag(UserTagDetail detail) {
        return new UserTag(
                detail.getUserTagId(),
                detail.getName(),
                detail.getTotalUsageCount()
        );
    }

    @Transactional(readOnly = true)
    public UserTagResponse getActiveUserTags(int userId) {
        List<UserTag> userTags = userTagQdslRepository.findActiveUserTagsByUserId(userId).stream()
                .map(this::toUserTag)
                .toList();

        return new UserTagResponse(userTags, userTags.size());
    }

    private UserTag toUserTag(UserTagRaw userTag) {
        return new UserTag(
                userTag.userTagId(),
                userTag.name(),
                userTag.count()
        );
    }

}
