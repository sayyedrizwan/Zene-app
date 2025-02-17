package com.rizwansayyed.zene.datastore.model

enum class VideoQualityEnum {
    `1440`, `720`, `480`
}

enum class VideoSpeedEnum {
    `0_25`, `0_5`, `1_0`, `1_5`, `2_0`
}

enum class YoutubePlayerState(val v: Int) {
    UNSTARTED(-1), ENDED(0), PLAYING(1), PAUSE(2), BUFFERING(3), VIDEO_CUED(5)
}
