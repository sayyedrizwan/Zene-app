package com.rizwansayyed.zene.di

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.work.Configuration
import androidx.work.WorkManager
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayerNotPlay
import com.rizwansayyed.zene.utils.NotificationViewManager
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.CRASH_CHANNEL
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.CRASH_CHANNEL_ID
import com.rizwansayyed.zene.utils.Utils.ifPlayerServiceNotRunning
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
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

        if (!ifPlayerServiceNotRunning()) {
            context.startService(Intent(context, PlayerService::class.java))
        }

//        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
//            context.cacheDir.deleteRecursively()
//
//            if (BuildConfig.DEBUG)
//                NotificationViewManager(this)
//                    .title("The App crash on ${thread.name} thread")
//                    .body(e.message ?: "No Crash Registered")
//                    .nIds(CRASH_CHANNEL_ID, CRASH_CHANNEL).generate()
//
//            Log.d(packageName, "App Crash Log: ${e.message}")
//
//            Intent(this, MainActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(this)
//            }
//        }

        CoroutineScope(Dispatchers.IO).launch {
            val playerData = musicPlayerData.first()?.apply {
                show = false
                songID = ""
            }

            if (playerData?.playType == MusicType.RADIO){
                musicPlayerData = flowOf(MusicPlayerData())
            } else {
                musicPlayerData = flowOf(playerData)

                delay(3.seconds)

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


//        FirebaseApp.initializeApp(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

}