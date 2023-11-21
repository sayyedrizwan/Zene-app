package com.rizwansayyed.zene.service.player.listener

import androidx.media3.common.MediaItem

interface PlayerServiceInterface {
    fun songInfoDownloading(b: Boolean){}
    fun songBuffering(b: Boolean) {}
    fun mediaItemUpdate(mediaItem: MediaItem) {}
    fun playingStateChange(){}
}