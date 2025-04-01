package com.movewave.common.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column(nullable = false)
    @Setter
    @Builder.Default
    private boolean isDeleted = false;

}
