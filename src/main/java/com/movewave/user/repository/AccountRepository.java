package com.movewave.user.repository;

import com.movewave.user.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByLoginIdAndIsDeletedFalse(String loginId);

    Optional<Account> findByIdAndIsDeletedFalse(Long accountId);
}
