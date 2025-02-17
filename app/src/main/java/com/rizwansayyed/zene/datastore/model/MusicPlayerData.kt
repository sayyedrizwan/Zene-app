package com.rizwansayyed.zene.datastore.model

import com.rizwansayyed.zene.data.model.ZeneMusicData

data class MusicPlayerData(
    val lists: List<ZeneMusicData?>,
    val data: ZeneMusicData?,
    val state: YoutubePlayerState?,
    var currentDuration: Int?,
    var totalDuration: Int?,
)