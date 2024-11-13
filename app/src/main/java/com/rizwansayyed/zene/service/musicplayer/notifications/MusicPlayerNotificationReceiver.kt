package com.rizwansayyed.zene.service.musicplayer.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.NEXT_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAY_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PREVIOUS_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_5S_BACK_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_5S_FORWARD_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand

class MusicPlayerNotificationReceiver : BroadcastReceiver() {

    companion object {
        const val PLAY_THE_MUSIC = "PLAY_THE_MUSIC"
        const val PAUSE_THE_MUSIC = "PAUSE_THE_MUSIC"
        const val GO_TO_THE_NEXT_MUSIC = "GO_TO_THE_NEXT_MUSIC"
        const val GO_TO_THE_PREVIOUS_MUSIC = "GO_TO_THE_PREVIOUS_MUSIC"
        const val MUSIC_FORWARD_5S = "MUSIC_FORWARD_5s"
        const val MUSIC_BACKWARD_5S = "MUSIC_BACKWARD_5s"
    }

    override fun onReceive(c: Context?, i: Intent?) {
        c ?: return
        i ?: return

        val value = i.getStringExtra(Intent.ACTION_MAIN)

        if (value == PLAY_THE_MUSIC) sendWebViewCommand(PLAY_VIDEO)
        if (value == PAUSE_THE_MUSIC) sendWebViewCommand(PAUSE_VIDEO)
        if (value == GO_TO_THE_NEXT_MUSIC) sendWebViewCommand(NEXT_SONG)
        if (value == GO_TO_THE_PREVIOUS_MUSIC) sendWebViewCommand(PREVIOUS_SONG)
        if (value == MUSIC_FORWARD_5S) sendWebViewCommand(SEEK_5S_FORWARD_VIDEO)
        if (value == MUSIC_BACKWARD_5S) sendWebViewCommand(SEEK_5S_BACK_VIDEO)
    }
}