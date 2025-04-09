package com.movewave.youtube.model.response;

import java.io.Serializable;

public record YouTubeResult(
        String videoTitle,
        String thumbnailUrl,
        String videoUrl,
        String musicUrl,
        String videoId
) implements Serializable {
}
