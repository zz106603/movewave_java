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

    /*
        플레이리스트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<FavoriteSongResponse> getList(Long accountId) {
        return favoriteSongRepository.findAllByAccountIdAndIsDeletedFalse(accountId).stream()
                .map(song -> new FavoriteSongResponse(
                        song.getVideoId(),
                        song.getTitle(),
                        song.getThumbnailUrl(),
                        song.getVideoUrl(),
                        song.getMusicUrl()))
                .toList();
    }

    /*
        플레이리스트 조회(Paging)
     */
    @Override
    public Page<FavoriteSongResponse> getPage(Long accountId, Pageable pageable) {
        return favoriteSongRepository.findAllByAccountIdAndIsDeletedFalse(accountId, pageable)
                .map(song -> new FavoriteSongResponse(
                        song.getVideoId(),
                        song.getTitle(),
                        song.getThumbnailUrl(),
                        song.getVideoUrl(),
                        song.getMusicUrl()
                ));
    }

    /*
        플레이리스트 저장
     */
    @Override
    @Transactional
    public void save(Long accountId, FavoriteSongRequest request) {
        Account account = accountRepository.getReferenceById(accountId);

        favoriteSongRepository.save(FavoriteSong.builder()
                .account(account)
                .videoId(request.videoId())
                .title(request.title())
                .thumbnailUrl(request.thumbnailUrl())
                .videoUrl(request.videoUrl())
                .musicUrl(request.musicUrl())
                .build());
    }

    /*
        플레이리스트 삭제
     */
    @Override
    @Transactional
    public void delete(Long accountId, String videoId) {
        FavoriteSong favoriteSong = favoriteSongRepository.findByAccountIdAndVideoIdAndIsDeletedFalse(accountId, videoId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.NOT_FOUND_ENTITY.format(FavoriteSong.class.getSimpleName(), videoId)));

        favoriteSong.setDeleted(true);
    }
}
