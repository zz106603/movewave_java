package com.movewave.favorite.controller;

import com.movewave.common.security.CustomUserDetails;
import com.movewave.favorite.model.request.FavoriteSongRequest;
import com.movewave.favorite.model.response.FavoriteSongResponse;
import com.movewave.favorite.service.FavoriteSongService;
import com.movewave.song.controller.SongApiUrls;
import com.movewave.user.domain.Account;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
public class FavoriteSongController {

    private final static String DEFAULT_PAGE = "0";
    private final static String DEFAULT_SIZE = "5";

    private final FavoriteSongService favoriteSongService;

    /**
     * 플레이리스트 목록 조회
     * @param userDetails (accountId)
     * @return List<FavoriteSongResponse>
     */
    @GetMapping(FavoriteSongApiUrls.FAVORITE_SONG_URL)
    public List<FavoriteSongResponse> getList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return favoriteSongService.getList(userDetails.getAccountId());
    }

    /**
     * 플레이리스트 목록 조회 (paging)
     * @param userDetails (accountId)
     * @param page
     * @param size
     * @return
     */
    @GetMapping(FavoriteSongApiUrls.FAVORITE_SONG_PAGE_URL)
    public Page<FavoriteSongResponse> getPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size
    ) {
        return favoriteSongService.getPage(userDetails.getAccountId(), PageRequest.of(page, size));
    }

    /**
     * 플레이리스트 저장
     * @param userDetails (accountId)
     * @param request (videoId, title, thumbnailUrl, videoUrl, musicUrl)
     */
    @PostMapping(FavoriteSongApiUrls.FAVORITE_SONG_URL)
    public void save(@AuthenticationPrincipal CustomUserDetails userDetails,
                     @RequestBody FavoriteSongRequest request) {
        favoriteSongService.save(userDetails.getAccountId(), request);
    }

    /**
     * 플레이리스트 삭제
     * @param userDetails (accountId)
     * @param videoId
     */
    @DeleteMapping(FavoriteSongApiUrls.FAVORITE_SONG_DELETE_URL)
    public void delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                       @PathVariable String videoId) {
        favoriteSongService.delete(userDetails.getAccountId(), videoId);
    }

}
