package com.movewave.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 기능을 활성화하기 위한 설정 클래스
 * - 엔티티의 생성일시, 수정일시 등을 자동으로 관리
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // JPA Auditing 활성화를 위한 빈 설정 클래스
}
