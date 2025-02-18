package com.rizwansayyed.zene.service.player

interface PlayerServiceInterface {
    fun toNextSong()
    fun toBackSong()
    fun pause()
    fun play()
    fun seekTo(v: Float)
}