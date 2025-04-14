package com.movewave.favorite.domain;

import com.movewave.common.domain.BaseEntity;
import com.movewave.user.domain.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_song")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FavoriteSong extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String videoId;

    @Column(nullable = false)
    private String title;

    private String thumbnailUrl;

    private String videoUrl;

    private String musicUrl;
}
