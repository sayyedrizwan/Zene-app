package com.rizwansayyed.zene.service.player.utils

import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.service.player.PlayerForegroundService

class SmartShuffle(private val service: PlayerForegroundService) {
    private val queue = mutableListOf<ZeneMusicData?>()
    private val history = mutableListOf<ZeneMusicData?>()
    private val maxHistorySize = service.songsLists.size / 2

    init {
        reshuffle()
    }

    private fun reshuffle() {
        queue.clear()
        queue.addAll(service.songsLists.toList().shuffled())
    }

    fun getNextSong(): ZeneMusicData? {
        if (queue.isEmpty()) reshuffle()

        var nextSong: ZeneMusicData? = null
        var attempts = 0

        while (attempts < 5) {
            val candidate = queue.removeFirstOrNull() ?: return null
            if (!history.contains(candidate) || history.size >= maxHistorySize) {
                nextSong = candidate
                break
            }
            queue.add(candidate)
            attempts++
        }

        if (nextSong != null) {
            history.add(nextSong)
            if (history.size > maxHistorySize) history.removeAt(0)
        }

        return nextSong
    }
}