package com.movewave.common.security;

import com.movewave.user.domain.Account;
import com.movewave.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginIdAndIsDeletedFalse(loginId).get();

        GrantedAuthority authority = new SimpleGrantedAuthority(account.getRole().name());
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        return new CustomUserDetails(
                account.getId(),
                account.getLoginId(),
                authorities,
                Collections.emptyMap()
        );
    }
}
