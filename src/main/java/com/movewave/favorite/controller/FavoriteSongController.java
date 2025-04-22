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
@RequestMapping("/api/favorite/song")
@RequiredArgsConstructor
public class FavoriteSongController {

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "5";

    private final FavoriteSongService favoriteSongService;

    /**
     * 즐겨찾기 목록 조회
     * @param userDetails 인증된 사용자 정보
     * @return 즐겨찾기 목록
     */
    @GetMapping("/")
    public List<FavoriteSongResponse> getList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return favoriteSongService.getList(userDetails.getAccountId());
    }

    /**
     * 즐겨찾기 목록 페이징 조회
     * @param userDetails 인증된 사용자 정보
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지당 항목 수 (기본값: 5)
     * @return 페이징된 즐겨찾기 목록
     */
    @GetMapping("/page")
    public Page<FavoriteSongResponse> getPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_SIZE) int size
    ) {
        return favoriteSongService.getPage(userDetails.getAccountId(), PageRequest.of(page, size));
    }

    /**
     * 즐겨찾기에 노래 추가
     * @param userDetails 인증된 사용자 정보
     * @param request 추가할 노래 정보
     */
    @PostMapping("/")
    public void createFavorite(@AuthenticationPrincipal CustomUserDetails userDetails,
                    @RequestBody FavoriteSongRequest request) {
        favoriteSongService.save(userDetails.getAccountId(), request);
    }

    /**
     * 즐겨찾기에서 노래 삭제
     * @param userDetails 인증된 사용자 정보
     * @param videoId 삭제할 노래의 비디오 ID
     */
    @DeleteMapping("/{videoId}")
    public void deleteFavorite(@AuthenticationPrincipal CustomUserDetails userDetails,
                      @PathVariable String videoId) {
        favoriteSongService.delete(userDetails.getAccountId(), videoId);
    }
}
