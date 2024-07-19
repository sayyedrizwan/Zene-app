package com.rizwansayyed.zene.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_BUFFERING
import com.rizwansayyed.zene.utils.Utils.moshi

object MusicServiceUtils {

    object Commands {
        const val PLAY_VIDEO = "play"
        const val PAUSE_VIDEO = "pause"
        const val SEEK_DURATION_VIDEO = "seek_duration"
        const val NEXT_SONG = "next_song"
        const val PREVIOUS_SONG = "previous_song"


        const val VIDEO_UNSTARTED = -1
        const val VIDEO_ENDED = 0
        const val VIDEO_PLAYING = 1
        const val VIDEO_PAUSE = 2
        const val VIDEO_BUFFERING = 3
        const val VIDEO_CUED = 5
    }


    private val WEB_VIEW_SERVICE_ACTION = "${context.packageName}_WEB_VIEW_SERVICE_ACTION"

    fun registerWebViewCommand(c: Context, listener: BroadcastReceiver?) {
        val l = IntentFilter(WEB_VIEW_SERVICE_ACTION)
        ContextCompat.registerReceiver(c, listener, l, ContextCompat.RECEIVER_EXPORTED)?.apply {
            setPackage(context.packageName)
        }
    }

    fun sendWebViewCommand(s: String, v: Int? = null) {
        Intent(WEB_VIEW_SERVICE_ACTION).apply {
            putExtra(Intent.ACTION_MAIN, s)
            if (v != null) putExtra(Intent.ACTION_MEDIA_EJECT, v)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.sendBroadcast(this)
        }
    }

    fun sendWebViewCommand(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
        val d = MusicPlayerData(list, m, VIDEO_BUFFERING, 0, false, 0, true)
        val json = moshi.adapter(MusicPlayerData::class.java).toJson(d)
        Intent(WEB_VIEW_SERVICE_ACTION).apply {
            putExtra(Intent.ACTION_MAIN, json)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.sendBroadcast(this)
        }
    }


}