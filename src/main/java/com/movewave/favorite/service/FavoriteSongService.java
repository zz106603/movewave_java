package com.movewave.favorite.service;

import com.movewave.favorite.model.request.FavoriteSongRequest;
import com.movewave.favorite.model.response.FavoriteSongResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteSongService {
    List<FavoriteSongResponse> getList(Long accountId);

    Page<FavoriteSongResponse> getPage(Long accountId, Pageable pageable);

    void save(Long accountId, FavoriteSongRequest request);

    void delete(Long accountId, String videoId);
}
