package com.rizwansayyed.zene.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.rizwansayyed.zene.di.BaseApp.Companion.context

object NavigationUtils {
    const val NAV_HOME = "home"
    const val NAV_SUBSCRIPTION = "subscription"
    const val NAV_SEARCH = "search"
    const val NAV_MY_MUSIC = "my_music"
    const val NAV_SETTINGS = "settings"
    const val NAV_PLAYLISTS = "playlist/{id}"
    const val NAV_USER_PLAYLISTS = "my_playlist/{id}"
    const val NAV_MOOD = "mood/{id}"
    const val NAV_ARTISTS = "artists/{id}"


    const val SYNC_DATA = "sync_data"


    private val NAV_ACTION = "${context.packageName}_NAV_ACTION"

    fun registerNavCommand(c: Activity, listener: BroadcastReceiver?) {
        val l = IntentFilter(NAV_ACTION)
        ContextCompat.registerReceiver(c, listener, l, ContextCompat.RECEIVER_EXPORTED)?.apply {
            setPackage(context.packageName)
        }
    }

    fun sendNavCommand(s: String) {
        Intent(NAV_ACTION).apply {
            putExtra(Intent.ACTION_MAIN, s)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.sendBroadcast(this)
        }
    }

}