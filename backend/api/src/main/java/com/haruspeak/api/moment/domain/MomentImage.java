package com.haruspeak.api.moment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "moment_images")
@Getter
public class MomentImage {

    @Id
    private Integer imageId;

    private Integer momentId;

    private String imageUrl;
}
