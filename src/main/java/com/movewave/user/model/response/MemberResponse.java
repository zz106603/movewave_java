package com.movewave.user.model.response;

import com.movewave.user.domain.Member;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String email,
        String name,
        String givenName,
        String familyName,
        String profileUrl,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
    public static MemberResponse from(Member member) {
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
