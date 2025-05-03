package com.haruspeak.api.moment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "moment_tag_names")
@Getter
public class MomentTagName {

    @Id
    private Integer momentTagId;

    private Integer momentId;

    private Integer userTagId;

    private Integer userId;

    private String name;
}
