package com.rizwansayyed.zene.service.implementation.listener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.content.IntentFilter
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.showPlayingSongOnLockScreenSettings
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.ui.extra.LockScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScreenLockAndOnListener(player: ExoPlayer) {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return

            if (intent.action.equals(Intent.ACTION_SCREEN_OFF)) CoroutineScope(Dispatchers.IO).launch {
                val isPlaying = withContext(Dispatchers.Main) { player.isPlaying }

                if (showPlayingSongOnLockScreenSettings.first() && isPlaying)
                    Intent(context, LockScreenActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context?.startActivity(this)
                    }


                if (isActive) cancel()
            }
        }

    }


    fun register() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        context.registerReceiver(receiver, filter)
    }

    fun unregister() {
        context.unregisterReceiver(receiver)
    }
}