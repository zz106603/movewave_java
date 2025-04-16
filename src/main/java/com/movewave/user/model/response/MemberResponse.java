package com.movewave.user.model.response;

import com.movewave.user.domain.Member;

import java.time.LocalDateTime;

/**
 * 회원 정보를 반환하기 위한 응답 객체입니다.
 */
public record MemberResponse(
        Long id,                    // 회원 ID
        String email,               // 이메일 
        String name,                // 이름
        String givenName,           // 이름(영문)
        String familyName,          // 성(영문)
        String profileUrl,          // 프로필 이미지 URL
        LocalDateTime createdAt,    // 생성일시
        LocalDateTime updateAt      // 수정일시
) {
    /**
     * Member 엔티티로부터 MemberResponse 객체를 생성합니다.
     * @param member 회원 엔티티
     * @return MemberResponse 객체
     */
    public static MemberResponse from(Member member) {
        if (member == null) {
            return null;
        }
        
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getGivenName(),
                member.getFamilyName(),
                member.getProfileUrl(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
