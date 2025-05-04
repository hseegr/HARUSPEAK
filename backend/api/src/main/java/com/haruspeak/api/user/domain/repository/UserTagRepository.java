package com.haruspeak.api.user.domain.repository;

import com.haruspeak.api.user.domain.UserTagDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTagRepository extends JpaRepository<UserTagDetail, Integer> {
    List<UserTagDetail> findByUserId(Integer userId);
}
