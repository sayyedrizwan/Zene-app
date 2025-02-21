package com.rizwansayyed.zene.datastore.model

import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.utils.MainUtils.formatDurationsForVideo

data class MusicPlayerData(
    val lists: List<ZeneMusicData?>,
    val data: ZeneMusicData?,
    var state: YoutubePlayerState?,
    var currentDuration: String?,
    var speed: String?,
    var totalDuration: String?,
) {
    fun currentDuration(): String {
        return formatDurationsForVideo(currentDuration?.toFloatOrNull() ?: 0f)
    }

    fun totalDuration(): String {
        return formatDurationsForVideo(totalDuration?.toFloatOrNull() ?: 0f)
    }
}