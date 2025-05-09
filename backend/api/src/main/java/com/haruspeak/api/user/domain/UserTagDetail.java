package com.haruspeak.api.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "user_tag_details")
@Immutable
@Getter
public class UserTagDetail {

    @Id
    private Integer userTagId;

    private Integer userId;

    private Integer tagId;

    private String name;

    private int totalUsageCount;

    private int score;
}
