package com.movewave.favorite.repository;

import com.movewave.favorite.domain.FavoriteSong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteSongRepository extends JpaRepository<FavoriteSong, Long> {

    List<FavoriteSong> findAllByAccountIdAndIsDeletedFalse(Long accountId);

    Page<FavoriteSong> findAllByAccountIdAndIsDeletedFalse(Long accountId, Pageable pageable);

    Optional<FavoriteSong> findByAccountIdAndVideoIdAndIsDeletedFalse(Long accountId, String videoId);
}
