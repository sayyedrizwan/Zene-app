package com.rizwansayyed.zene.data.db.model

import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems

data class MusicPlayerData(
    val list: List<ZeneMusicDataItems>?,
    val player: ZeneMusicDataItems?,
    val state: Int?,
    var currentDuration: Int?,
    var isPlaying: Boolean?,
    var totalDuration: Int?,
    var isBuffering: Boolean?,
)