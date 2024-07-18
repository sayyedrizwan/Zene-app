package com.rizwansayyed.zene.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.NEW_VIDEO

object MusicServiceUtils {

    object Commands {
        const val PLAY_VIDEO = "play"
        const val PAUSE_VIDEO = "play"
        const val NEW_VIDEO = "new_"
    }


    private val WEB_VIEW_SERVICE_ACTION = "${context.packageName}_WEB_VIEW_SERVICE_ACTION"

    fun registerWebViewCommand(c: Context, listener: BroadcastReceiver?) {
        val l = IntentFilter(WEB_VIEW_SERVICE_ACTION)
        ContextCompat.registerReceiver(c, listener, l, ContextCompat.RECEIVER_EXPORTED)?.apply {
            setPackage(context.packageName)
        }
    }

    fun sendWebViewCommand(s: String) {
        Intent(WEB_VIEW_SERVICE_ACTION).apply {
            putExtra(Intent.ACTION_MAIN, s)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.sendBroadcast(this)
        }
    }

    fun sendWebViewCommandSongID(id: String) {
        Intent(WEB_VIEW_SERVICE_ACTION).apply {
            putExtra(Intent.ACTION_MAIN, "${NEW_VIDEO}$id")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.sendBroadcast(this)
        }
    }


}