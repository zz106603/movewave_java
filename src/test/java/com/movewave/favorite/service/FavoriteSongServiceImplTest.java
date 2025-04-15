package com.movewave.favorite.service;

import com.movewave.common.exception.base.ErrorMessages;
import com.movewave.favorite.domain.FavoriteSong;
import com.movewave.favorite.model.request.FavoriteSongRequest;
import com.movewave.favorite.model.response.FavoriteSongResponse;
import com.movewave.favorite.repository.FavoriteSongRepository;
import com.movewave.user.domain.Account;
import com.movewave.user.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FavoriteSongServiceImplTest {

    @Mock
    private FavoriteSongRepository favoriteSongRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private FavoriteSongServiceImpl favoriteSongService;

    private Account account;
    private FavoriteSong favoriteSong;
    private FavoriteSongRequest request;

    private static final Long TEST_ACCOUNT_ID = 1L;
    private static final String TEST_VIDEO_ID = "testVideoId";
    private static final String NON_EXISTENT_ID = "nonExistentId";
    private static final String TEST_TITLE = "Test Title";
    private static final String TEST_THUMBNAIL_URL = "http://test.thumbnail.url";
    private static final String TEST_VIDEO_URL = "http://test.video.url";
    private static final String TEST_MUSIC_URL = "http://test.music.url";

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(TEST_ACCOUNT_ID)
                .build();

        favoriteSong = FavoriteSong.builder()
                .account(account)
                .videoId(TEST_VIDEO_ID)
                .title(TEST_TITLE)
                .thumbnailUrl(TEST_THUMBNAIL_URL)
                .videoUrl(TEST_VIDEO_URL)
                .musicUrl(TEST_MUSIC_URL)
                .build();

        request = new FavoriteSongRequest(
                TEST_VIDEO_ID,
                TEST_TITLE,
                TEST_THUMBNAIL_URL,
                TEST_VIDEO_URL,
                TEST_MUSIC_URL
        );
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회 성공")
    void getList_Success() {
        // given
        given(favoriteSongRepository.findAllByAccountIdAndIsDeletedFalse(account.getId()))
                .willReturn(List.of(favoriteSong));

        // when
        List<FavoriteSongResponse> result = favoriteSongService.getList(account.getId());

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).videoId()).isEqualTo(favoriteSong.getVideoId());
        assertThat(result.get(0).title()).isEqualTo(favoriteSong.getTitle());
    }

    @Test
    @DisplayName("즐겨찾기 목록 페이징 조회 성공")
    void getPage_Success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<FavoriteSong> favoriteSongPage = new PageImpl<>(List.of(favoriteSong));
        given(favoriteSongRepository.findAllByAccountIdAndIsDeletedFalse(account.getId(), pageable))
                .willReturn(favoriteSongPage);

        // when
        Page<FavoriteSongResponse> result = favoriteSongService.getPage(account.getId(), pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).videoId()).isEqualTo(favoriteSong.getVideoId());
    }

    @Test
    @DisplayName("즐겨찾기 노래 추가 성공")
    void save_Success() {
        // given
        given(accountRepository.getReferenceById(account.getId())).willReturn(account);

        // when
        favoriteSongService.save(account.getId(), request);

        // then
        verify(favoriteSongRepository).save(any(FavoriteSong.class));
    }

    @Test
    @DisplayName("즐겨찾기 노래 삭제 성공")
    void delete_Success() {
        // given
        given(favoriteSongRepository.findByAccountIdAndVideoIdAndIsDeletedFalse(account.getId(), favoriteSong.getVideoId()))
                .willReturn(Optional.of(favoriteSong));

        // when
        favoriteSongService.delete(account.getId(), favoriteSong.getVideoId());

        // then
        assertThat(favoriteSong.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("즐겨찾기 노래 삭제 실패 - 존재하지 않는 노래")
    void delete_Fail_NotFound() {
        // given
        given(favoriteSongRepository.findByAccountIdAndVideoIdAndIsDeletedFalse(account.getId(), NON_EXISTENT_ID))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteSongService.delete(account.getId(), NON_EXISTENT_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(ErrorMessages.NOT_FOUND_ENTITY.format(FavoriteSong.class.getSimpleName(), NON_EXISTENT_ID));
    }
}
