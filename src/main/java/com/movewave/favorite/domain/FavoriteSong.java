package com.movewave.favorite.domain;

import com.movewave.common.domain.BaseEntity;
import com.movewave.user.domain.Account;
import jakarta.persistence.*;
import lombok.*;

/**
 * 즐겨찾기 노래 엔티티
 */
@Entity
@Table(
    name = "favorite_song",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_favorite_song_account_video_deleted",
            columnNames = {"account_id", "video_id", "is_deleted"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FavoriteSong extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "video_id", nullable = false, length = 50)
    private String videoId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String thumbnailUrl;

    @Column(length = 500)
    private String videoUrl;

    @Column(length = 500) 
    private String musicUrl;
}
