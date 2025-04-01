package com.movewave.music.repository;

import com.movewave.music.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByEmotion(String emotion);
}