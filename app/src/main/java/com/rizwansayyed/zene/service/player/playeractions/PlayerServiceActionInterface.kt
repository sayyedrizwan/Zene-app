package com.rizwansayyed.zene.service.player.playeractions

import androidx.media3.common.MediaItem
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem

interface PlayerServiceActionInterface {
    suspend fun addMultipleItemsAndPlay(list: Array<MusicData?>?, position: Int)
    suspend fun updatePlaying(mediaItem: MediaItem?)
    suspend fun startPlaying(music: MusicData?, list: Array<MusicData?>?, position: Int)
    suspend fun playLiveRadio(radio: OnlineRadioResponseItem)

    suspend fun addItemToNext(music: MusicData)
    suspend fun addItemToEnd(music: MusicData)
}