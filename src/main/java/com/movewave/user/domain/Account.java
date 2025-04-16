package com.movewave.user.domain;

import com.movewave.common.domain.BaseEntity;
import com.movewave.user.domain.enums.AccountLoginType;
import com.movewave.user.domain.enums.AccountRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 사용자 계정 정보를 관리하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // PUBLIC -> PROTECTED로 변경하여 무분별한 객체 생성 방지
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Account extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(length = 255) // 비밀번호 길이 제한 추가
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountLoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role;
    
}
