package com.rizwansayyed.zene.service.musicplayer

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import com.rizwansayyed.zene.data.api.APIHttpService.youtubeSearchVideoRegion
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_BUFFERING
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_ENDED
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_PLAYING
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_UNSTARTED
import com.rizwansayyed.zene.service.musicplayer.notifications.MusicPlayerNotifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicPlayerInterface(
    private val context: MusicPlayService,
    private val playNextSong: () -> Unit,
    private val playbackSpeed: (Boolean) -> Unit,
    private val videoUnstarted: () -> Unit,
    private val playAVideo: (String) -> Unit
) {

    @SuppressLint("MissingPermission")
    @JavascriptInterface
    fun playerState(v: Int, duration: Int, currentDuration: Int, updatePlayback: Boolean) =
        CoroutineScope(Dispatchers.IO).launch {
            if (v == VIDEO_ENDED) playNextSong()

            val d = musicPlayerDB.first()
            d?.isBuffering = v == VIDEO_BUFFERING
            d?.isPlaying = false
            if (v == VIDEO_PLAYING) {
                d?.isPlaying = true
                if (updatePlayback) playbackSpeed(true)

                playbackSpeed(false)
            } else if (v == VIDEO_UNSTARTED) videoUnstarted()
            musicPlayerDB = flowOf(d)

            MusicPlayerNotifications(
                context, v == VIDEO_PLAYING, d?.player, duration, currentDuration
            ).generate()

            if (isActive) cancel()
        }

    @JavascriptInterface
    fun playerDuration(current: Int, total: Int) = CoroutineScope(Dispatchers.IO).launch {
        val d = musicPlayerDB.first()
        d?.totalDuration = total
        d?.currentDuration = current
        musicPlayerDB = flowOf(d)
        if (isActive) cancel()
    }

    @JavascriptInterface
    fun regionSongError() = CoroutineScope(Dispatchers.IO).launch {
        val d = musicPlayerDB.first()
        val videoID = youtubeSearchVideoRegion(d?.player?.name ?: "", d?.player?.artists ?: "")
        if (videoID.length > 3) playAVideo(videoID)

        if (isActive) cancel()
    }
}