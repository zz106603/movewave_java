package com.movewave.user.repository;

import com.movewave.user.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Account 엔티티에 대한 데이터베이스 작업을 처리하는 리포지토리입니다.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * 로그인 ID로 삭제되지 않은 계정을 조회합니다.
     * @param loginId 조회할 로그인 ID
     * @return 조회된 계정 (Optional)
     */
    Optional<Account> findByLoginIdAndIsDeletedFalse(String loginId);

    /**
     * 계정 ID로 삭제되지 않은 계정을 조회합니다.
     * @param accountId 조회할 계정 ID
     * @return 조회된 계정 (Optional)
     */
    Optional<Account> findByIdAndIsDeletedFalse(Long accountId);
}
