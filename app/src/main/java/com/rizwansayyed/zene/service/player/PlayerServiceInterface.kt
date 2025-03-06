package com.rizwansayyed.zene.service.player

import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.service.player.utils.SleepTimerEnum

interface PlayerServiceInterface {
    fun sleepTimer(minutes: SleepTimerEnum)
    fun toNextSong()
    fun toBackSong()
    fun pause()
    fun play()
    fun seekTo(v: Long)
    fun playbackRate(v: String)
    fun addListsToNext(list: List<ZeneMusicData>)
    fun addListsToQueue(list: List<ZeneMusicData>)
}