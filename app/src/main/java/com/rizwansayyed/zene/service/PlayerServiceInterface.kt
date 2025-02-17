package com.rizwansayyed.zene.service

interface PlayerServiceInterface {
    fun pause()
    fun play()
    fun seekTo(v: Float)
}