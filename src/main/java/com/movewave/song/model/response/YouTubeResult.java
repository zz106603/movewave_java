package com.movewave.song.model.response;

import java.io.Serializable;

public record YouTubeResult(
        String title,
        String thumbnailUrl,
        String videoUrl
) implements Serializable {
}
