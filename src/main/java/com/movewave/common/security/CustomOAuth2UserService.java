package com.movewave.common.security;

import com.movewave.user.domain.Account;
import com.movewave.user.domain.Member;
import com.movewave.user.domain.enums.AccountLoginType;
import com.movewave.user.domain.enums.AccountRole;
import com.movewave.user.repository.AccountRepository;
import com.movewave.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

/**
 * OAuth2 사용자 인증을 처리하는 커스텀 서비스입니다.
 * Google OAuth2 로그인 시 사용자 정보를 가져와서 계정을 생성하거나 조회합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

    /**
     * OAuth2 사용자 정보를 로드하고 계정을 생성하거나 조회합니다.
     * @param userRequest OAuth2 사용자 요청 정보
     * @return CustomUserDetails 객체
     * @throws OAuth2AuthenticationException 인증 실패 시 발생
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        OAuth2UserInfo userInfo = extractUserInfo(oAuth2User);
        
        Account account = getOrCreateAccount(userInfo);
        Collection<GrantedAuthority> authorities = createAuthorities(account);
        
        return new CustomUserDetails(
                account.getId(),
                account.getLoginId(),
                authorities,
                oAuth2User.getAttributes()
        );
    }

    /**
     * OAuth2User로부터 사용자 정보를 추출합니다.
     * @param oAuth2User OAuth2 사용자 객체
     * @return OAuth2UserInfo 객체
     */
    private OAuth2UserInfo extractUserInfo(OAuth2User oAuth2User) {
        return OAuth2UserInfo.builder()
                .email(oAuth2User.getAttribute("email"))
                .name(oAuth2User.getAttribute("name"))
                .givenName(oAuth2User.getAttribute("given_name"))
                .familyName(oAuth2User.getAttribute("family_name"))
                .profileUrl(oAuth2User.getAttribute("picture"))
                .build();
    }

    /**
     * 이메일로 계정을 조회하거나 새로 생성합니다.
     * @param userInfo OAuth2 사용자 정보
     * @return Account 객체
     */
    private Account getOrCreateAccount(OAuth2UserInfo userInfo) {
        return accountRepository.findByLoginIdAndIsDeletedFalse(userInfo.getEmail())
                .orElseGet(() -> createNewAccount(userInfo));
    }

    /**
     * 새로운 계정을 생성합니다.
     * @param userInfo OAuth2 사용자 정보
     * @return 생성된 Account 객체
     * @throws OAuth2AuthenticationException 계정 생성 실패 시 발생
     */
    private Account createNewAccount(OAuth2UserInfo userInfo) {
        try {
            Member member = createMember(userInfo);
            return createAccount(userInfo.getEmail(), member);
        } catch (Exception e) {
            log.error("Error occurred during OAuth2 registration: {}", e.getMessage(), e);
            throw new OAuth2AuthenticationException("Create Account Failed");
        }
    }

    /**
     * 회원을 생성합니다.
     * @param userInfo OAuth2 사용자 정보
     * @return 생성된 Member 객체
     */
    private Member createMember(OAuth2UserInfo userInfo) {
        return memberRepository.save(Member.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .givenName(userInfo.getGivenName())
                .familyName(userInfo.getFamilyName())
                .profileUrl(userInfo.getProfileUrl())
                .build());
    }

    /**
     * 계정을 생성합니다.
     * @param email 사용자 이메일
     * @param member 회원 객체
     * @return 생성된 Account 객체
     */
    private Account createAccount(String email, Member member) {
        return accountRepository.save(Account.builder()
                .loginId(email)
                .password("oauth_password")
                .member(member)
                .loginType(AccountLoginType.GOOGLE)
                .role(AccountRole.ROLE_USER)
                .build());
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

/**
 * OAuth2 사용자 정보를 담는 DTO 클래스입니다.
 */
@lombok.Builder
@lombok.Getter
class OAuth2UserInfo {
    private final String email;
    private final String name;
    private final String givenName;
    private final String familyName;
    private final String profileUrl;
}
