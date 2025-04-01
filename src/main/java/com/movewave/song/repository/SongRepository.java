package com.movewave.song.repository;

import com.movewave.song.domain.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    @Query("SELECT s FROM Song s WHERE s.emotion = :emotion AND s.isDeleted = false ORDER BY FUNCTION('RAND')")
    List<Song> findRandomSongsByEmotion(@Param("emotion") String emotion, Pageable pageable);
}