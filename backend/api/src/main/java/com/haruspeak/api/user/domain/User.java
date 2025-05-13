package com.haruspeak.api.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * users
 */
@Entity
@Table(name="users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "sns_type", nullable = false, length = 10)
    private String snsType = "google"; // 현재는 구글로그인만 지원

    @Column(name = "sns_id", nullable = false, length = 50, unique = true)
    private String snsId;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private int isDeleted = 0;


    /**
     * PK 제외 모든 필드
     */
    @Builder
    public User (String snsType, String snsId, String email, String name, int isDeleted) {
        this.snsType = snsType;
        this.snsId = snsId;
        this.email = email;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    /**
     * 회원 가입 시 사용
     */
    public static User create(String snsId, String email, String name) {
        return User.builder()
                .snsType("google")
                .snsId(snsId)
                .email(email)
                .name(name)
                .isDeleted(0)
                .build();
    }

}
