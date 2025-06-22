package com.rizwansayyed.zene.datastore.model

import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.utils.MainUtils.formatDurationsForVideo
import com.rizwansayyed.zene.utils.MainUtils.formatMSDurationsForVideo

data class MusicPlayerData(
    var lists: List<ZeneMusicData?>?,
    var data: ZeneMusicData?,
    var state: YoutubePlayerState?,
    var currentDuration: String?,
    var speed: String?,
    var totalDuration: String?,
) {
    fun isPlaying(): Boolean {
        return state == YoutubePlayerState.PLAYING
    }

    fun currentDuration(): String {
        if (data?.type() == MusicDataTypes.SONGS)
            return formatDurationsForVideo(currentDuration?.toFloatOrNull() ?: 0f)

        return formatMSDurationsForVideo(currentDuration?.toFloatOrNull() ?: 0f)
    }

    fun totalDuration(): String {
        if (data?.type() == MusicDataTypes.SONGS)
            return formatDurationsForVideo(totalDuration?.toFloatOrNull() ?: 0f)

        return formatMSDurationsForVideo(totalDuration?.toFloatOrNull() ?: 0f)
    }
}