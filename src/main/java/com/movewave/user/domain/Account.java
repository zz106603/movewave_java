package com.movewave.user.domain;

import com.movewave.common.domain.BaseEntity;
import com.movewave.user.domain.enums.AccountLoginType;
import com.movewave.user.domain.enums.AccountRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Account extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String loginId;

    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountLoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole roles;
}
