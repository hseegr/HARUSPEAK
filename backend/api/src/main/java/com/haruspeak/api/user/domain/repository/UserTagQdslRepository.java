package com.haruspeak.api.user.domain.repository;

import com.haruspeak.api.user.dto.UserTagRaw;

import java.util.List;

public interface UserTagQdslRepository {
    List<UserTagRaw> findActiveUserTagsByUserId(int userId);
}
