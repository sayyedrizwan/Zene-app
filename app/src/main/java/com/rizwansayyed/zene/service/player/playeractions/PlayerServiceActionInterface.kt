package com.rizwansayyed.zene.service.player.playeractions

import com.rizwansayyed.zene.domain.MusicData

interface PlayerServiceActionInterface {
    suspend fun addMultipleItemsAndPlay(list: Array<MusicData>?, position: Int)
}