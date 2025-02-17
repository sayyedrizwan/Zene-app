package com.rizwansayyed.zene.datastore.model

import com.rizwansayyed.zene.data.model.ZeneMusicData

data class MusicPlayerData(
    val lists: List<ZeneMusicData?>,
    val data: ZeneMusicData?,
    var state: YoutubePlayerState?,
    var currentDuration: String?,
    var totalDuration: String?,
)