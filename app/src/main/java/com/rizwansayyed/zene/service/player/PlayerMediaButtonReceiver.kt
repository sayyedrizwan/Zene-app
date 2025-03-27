package com.rizwansayyed.zene.service.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.PlaybackStateCompat
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.utils.MainUtils.toast

class PlayerMediaButtonReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context?, i: Intent?) {
        val intent = i?.getLongExtra(Intent.ACTION_VIEW, 0L)

        when (intent) {
            PlaybackStateCompat.ACTION_PLAY -> getPlayerS()?.play()
            PlaybackStateCompat.ACTION_PAUSE -> getPlayerS()?.pause()
            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS -> getPlayerS()?.toBackSong()
            PlaybackStateCompat.ACTION_SKIP_TO_NEXT -> getPlayerS()?.toNextSong()
        }
    }
}