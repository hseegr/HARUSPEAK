package com.haruspeak.api.user.application;

import com.haruspeak.api.common.exception.user.UserRegisterException;
import com.haruspeak.api.user.domain.User;
import com.haruspeak.api.user.domain.UserTagDetail;
import com.haruspeak.api.user.domain.repository.UserRepository;
import com.haruspeak.api.user.domain.repository.UserTagRepository;
import com.haruspeak.api.user.dto.UserTag;
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
    public List<UserTag> getUserTags(int userId) {
        return userTagRepository.findByUserId(userId).stream()
                .map(detail -> new UserTag(
                        detail.getUserTagId(),
                        detail.getName(),
                        detail.getTotalUsageCount()
                ))
                .toList();
    }

}
