package com.movewave.favorite.repository;

import com.movewave.favorite.domain.FavoriteSong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 즐겨찾기 노래 Repository
 */
public interface FavoriteSongRepository extends JpaRepository<FavoriteSong, Long> {

    /**
     * 사용자의 삭제되지 않은 모든 즐겨찾기 노래를 조회합니다.
     * @param accountId 사용자 ID
     * @return 즐겨찾기 노래 목록
     */
    List<FavoriteSong> findAllByAccountIdAndIsDeletedFalse(Long accountId);

    /**
     * 사용자의 삭제되지 않은 즐겨찾기 노래를 페이징하여 조회합니다.
     * @param accountId 사용자 ID
     * @param pageable 페이징 정보
     * @return 페이징된 즐겨찾기 노래 목록
     */
    Page<FavoriteSong> findAllByAccountIdAndIsDeletedFalse(Long accountId, Pageable pageable);

    /**
     * 사용자의 특정 비디오 ID에 해당하는 삭제되지 않은 즐겨찾기 노래를 조회합니다.
     * @param accountId 사용자 ID
     * @param videoId 비디오 ID
     * @return 즐겨찾기 노래 Optional
     */
    Optional<FavoriteSong> findByAccountIdAndVideoIdAndIsDeletedFalse(Long accountId, String videoId);
}
