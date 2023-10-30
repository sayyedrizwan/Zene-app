package com.rizwansayyed.zene.service.player.playeractions

import androidx.media3.common.MediaItem
import com.rizwansayyed.zene.domain.MusicData

interface PlayerServiceActionInterface {
    suspend fun addMultipleItemsAndPlay(list: Array<MusicData?>?, position: Int)
    suspend fun updatePlaying(mediaItem: MediaItem?)
    suspend fun startPlaying(music: MusicData?, list: Array<MusicData?>?, position: Int)
}