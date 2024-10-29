package com.rizwansayyed.zene.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SLEEP_PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.Utils.toast

class AlarmListenerReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context?, i: Intent?) {
        c ?: return
        i ?: return

        val type = i.getIntExtra(Intent.ACTION_MAIN, -1)
        if (type == 0) sendWebViewCommand(SLEEP_PAUSE_VIDEO)
    }
}