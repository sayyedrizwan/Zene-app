package com.rizwansayyed.zene.di

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayerNotPlay
import com.rizwansayyed.zene.utils.AppCrashHandler
import com.rizwansayyed.zene.utils.NotificationViewManager
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.CRASH_CHANNEL
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.CRASH_CHANNEL_ID
import com.rizwansayyed.zene.utils.Utils.ifPlayerServiceNotRunning
import com.rizwansayyed.zene.utils.Utils.timestampDifference
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds


@HiltAndroidApp
class ApplicationModule : Application(), Configuration.Provider {
    companion object {
        @Volatile
        lateinit var context: ApplicationModule
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var player: ExoPlayer

    override fun onCreate() {
        super.onCreate()
        context = this
        FirebaseApp.initializeApp(this)
        AppCrashHandler(this)


        CoroutineScope(Dispatchers.IO).launch {
            delay(2.seconds)

            if (!ifPlayerServiceNotRunning()) {
                context.startService(Intent(context, PlayerService::class.java))
            }
            if (isActive) cancel()
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(2.seconds)
            val playerData = musicPlayerData.first()?.apply {
                show = false
                songID = ""
            }

            if (playerData?.playType == MusicType.RADIO) {
                delay(1.seconds)
                musicPlayerData = flowOf(MusicPlayerData())
            } else {
                musicPlayerData = flowOf(playerData)

                delay(6.seconds)

                if ((playerData?.songsLists?.size ?: 0) > 0) {
                    var songPosition = 0
                    playerData?.songsLists?.forEachIndexed { i, musicData ->
                        if (musicData?.pId == playerData.v?.songID) songPosition = i
                    }

                    addAllPlayerNotPlay(playerData?.songsLists?.toTypedArray(), songPosition)
                }
            }
            File(context.filesDir, "offline_songs").deleteRecursively()
        }

    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

}