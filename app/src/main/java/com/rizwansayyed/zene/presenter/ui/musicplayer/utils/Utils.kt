package com.rizwansayyed.zene.presenter.ui.musicplayer.utils

import android.util.Log
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds

object Utils {

    fun formatExoplayerDuration(duration: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60

        return when {
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
            else -> String.format("%02d:%02d", minutes, seconds)
        }
    }


    private var lastTimestamp = System.currentTimeMillis() - 4000
    fun onClickOn3SecDelay(duration: Int = 3, run: () -> Unit) {
        if (abs(System.currentTimeMillis() - lastTimestamp) <= duration.seconds.inWholeMilliseconds) return
        lastTimestamp = System.currentTimeMillis()
        run()
    }


    fun areSongNamesEqual(songName1: String, songName2: String): Boolean {
        val song1 = songName1.split(" ")
        val song2 = songName2.split(" ")
        val commonWords = song2.intersect(song1.toSet())

        if (commonWords.size >= 3) return true
        return false
    }

    enum class MusicViewType {
        MUSIC, VIDEO, LYRICS
    }
}