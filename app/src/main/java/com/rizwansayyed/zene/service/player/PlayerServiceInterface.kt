package com.rizwansayyed.zene.service.player

import com.rizwansayyed.zene.service.player.utils.SleepTimerEnum

interface PlayerServiceInterface {
    fun sleepTimer(minutes: SleepTimerEnum)
    fun toNextSong()
    fun toBackSong()
    fun pause()
    fun play()
    fun seekTo(v: Float)
    fun playbackRate(v: String)
}