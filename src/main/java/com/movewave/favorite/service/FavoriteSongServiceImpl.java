package com.movewave.favorite.service;

import com.movewave.common.exception.base.ErrorMessages;
import com.movewave.favorite.domain.FavoriteSong;
import com.movewave.favorite.model.request.FavoriteSongRequest;
import com.movewave.favorite.model.response.FavoriteSongResponse;
import com.movewave.favorite.repository.FavoriteSongRepository;
import com.movewave.user.domain.Account;
import com.movewave.user.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteSongServiceImpl implements FavoriteSongService {

    private final FavoriteSongRepository favoriteSongRepository;
    private final AccountRepository accountRepository;

    /**
     * 사용자의 즐겨찾기 목록을 조회합니다.
     * @param accountId 사용자 ID
     * @return 즐겨찾기 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<FavoriteSongResponse> getFavorites(Long accountId) {
        return favoriteSongRepository.findAllByAccountIdAndIsDeletedFalse(accountId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    /**
     * 사용자의 즐겨찾기 목록을 페이징하여 조회합니다.
     * @param accountId 사용자 ID
     * @param pageable 페이징 정보
     * @return 페이징된 즐겨찾기 목록
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavoriteSongResponse> getFavoritesPage(Long accountId, Pageable pageable) {
        return favoriteSongRepository.findAllByAccountIdAndIsDeletedFalse(accountId, pageable)
                .map(this::convertToResponse);
    }

    /**
     * 즐겨찾기에 노래를 추가합니다.
     * @param accountId 사용자 ID
     * @param request 추가할 노래 정보
     */
    @Override
    @Transactional
    public void createFavorite(Long accountId, FavoriteSongRequest request) {
        Account account = accountRepository.getReferenceById(accountId);
        
        FavoriteSong favoriteSong = FavoriteSong.builder()
                .account(account)
                .videoId(request.videoId())
                .title(request.title())
                .thumbnailUrl(request.thumbnailUrl())
                .videoUrl(request.videoUrl())
                .musicUrl(request.musicUrl())
                .build();
                
        favoriteSongRepository.save(favoriteSong);
    }

    /**
     * 즐겨찾기에서 노래를 삭제합니다.
     * @param accountId 사용자 ID
     * @param videoId 삭제할 노래의 비디오 ID
     * @throws EntityNotFoundException 해당 노래를 찾을 수 없는 경우
     */
    @Override
    @Transactional
    public void deleteFavorite(Long accountId, String videoId) {
        FavoriteSong favoriteSong = favoriteSongRepository.findByAccountIdAndVideoIdAndIsDeletedFalse(accountId, videoId)
                .orElseThrow(() -> new EntityNotFoundException(
                    ErrorMessages.NOT_FOUND_ENTITY.format(FavoriteSong.class.getSimpleName(), videoId)
                ));

        favoriteSong.setDeleted(true);
    }

    private FavoriteSongResponse convertToResponse(FavoriteSong song) {
        return new FavoriteSongResponse(
                song.getVideoId(),
                song.getTitle(), 
                song.getThumbnailUrl(),
                song.getVideoUrl(),
                song.getMusicUrl()
        );
    }
}
