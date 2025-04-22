package com.movewave.favorite.service;

import com.movewave.favorite.model.request.FavoriteSongRequest;
import com.movewave.favorite.model.response.FavoriteSongResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 즐겨찾기 노래 관련 서비스 인터페이스
 */
public interface FavoriteSongService {

    /**
     * 사용자의 즐겨찾기 목록을 조회합니다.
     * @param accountId 사용자 ID
     * @return 즐겨찾기 목록
     */
    List<FavoriteSongResponse> getFavorites(Long accountId);

    /**
     * 사용자의 즐겨찾기 목록을 페이징하여 조회합니다.
     * @param accountId 사용자 ID
     * @param pageable 페이징 정보
     * @return 페이징된 즐겨찾기 목록
     */
    Page<FavoriteSongResponse> getFavoritesPage(Long accountId, Pageable pageable);

    /**
     * 즐겨찾기에 노래를 추가합니다.
     * @param accountId 사용자 ID
     * @param request 추가할 노래 정보
     */
    void createFavorite(Long accountId, FavoriteSongRequest request);

    /**
     * 즐겨찾기에서 노래를 삭제합니다.
     * @param accountId 사용자 ID
     * @param videoId 삭제할 노래의 비디오 ID
     */
    void deleteFavorite(Long accountId, String videoId);
}
