package com.rizwansayyed.zene.data.db.model

import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import java.util.Locale

data class MusicPlayerData(
    val list: List<ZeneMusicDataItems>?,
    val player: ZeneMusicDataItems?,
    val state: Int?,
    var currentDuration: Int?,
    var isPlaying: Boolean?,
    var totalDuration: Int?,
    var isBuffering: Boolean?,
) {
    fun formatCurrentDuration(): String {
        val seconds = (currentDuration ?: 0)
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return if (hours > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, remainingSeconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
        }
    }

    fun formatTotalDuration(): String {
        val seconds = (totalDuration ?: 0)
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return if (hours > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, remainingSeconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
        }
    }
}