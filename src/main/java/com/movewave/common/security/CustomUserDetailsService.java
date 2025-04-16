package com.movewave.common.security;

import com.movewave.common.exception.base.ErrorMessages;
import com.movewave.user.domain.Account;
import com.movewave.user.repository.AccountRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

/**
 * 일반 로그인 사용자 인증을 처리하는 커스텀 서비스입니다.
 * 사용자 ID로 계정을 조회하고 인증 정보를 생성합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * 사용자 ID로 계정을 조회하고 인증 정보를 생성합니다.
     * @param loginId 로그인 ID
     * @return CustomUserDetails 객체
     * @throws UsernameNotFoundException 계정을 찾을 수 없을 때 발생
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginIdAndIsDeletedFalse(loginId)
                .orElseThrow(() -> new EntityNotFoundException(
                    ErrorMessages.NOT_FOUND_ENTITY.format(Account.class.getSimpleName(), loginId)
                ));

        Collection<GrantedAuthority> authorities = createAuthorities(account);

        return new CustomUserDetails(
                account.getId(),
                account.getLoginId(),
                authorities,
                Collections.emptyMap()
        );
    }

    /**
     * 계정의 권한 정보를 생성합니다.
     * @param account 계정 객체
     * @return 권한 컬렉션
     */
    private Collection<GrantedAuthority> createAuthorities(Account account) {
        return Collections.singletonList(
                new SimpleGrantedAuthority(account.getRole().name())
        );
    }
}
