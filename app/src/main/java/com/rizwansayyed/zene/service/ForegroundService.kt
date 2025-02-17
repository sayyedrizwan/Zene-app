package com.rizwansayyed.zene.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.MainUtils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundService : Service() {

    @Inject
    lateinit var zeneAPI: ZeneAPIImplementation

    private var currentPlayingSong: ZeneMusicData? = null
    private var songsLists: Array<ZeneMusicData?> = emptyArray()

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val musicData = intent?.getStringExtra(Intent.ACTION_VIEW) ?: "{}"
        val musicList = intent?.getStringExtra(Intent.ACTION_RUN) ?: "[]"
        currentPlayingSong = moshi.adapter(ZeneMusicData::class.java).fromJson(musicData)
        songsLists =
            moshi.adapter(Array<ZeneMusicData?>::class.java).fromJson(musicList) ?: emptyArray()

        getSimilarSongInfo()

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        EmptyServiceNotification.generate(this)
    }

    private fun getSimilarSongInfo() = CoroutineScope(Dispatchers.IO).launch {
        currentPlayingSong ?: return@launch

        fun saveEmpty() {
            val d = MusicPlayerData(
                songsLists.asList(), currentPlayingSong, YoutubePlayerState.UNSTARTED, 0, 0
            )
            musicPlayerDB = flowOf(d)
        }

        if (songsLists.size > 1) {
            saveEmpty()
            return@launch
        }

        (currentPlayingSong!!.id ?: "nnn").toast()

        try {
            val lists = zeneAPI.similarPlaylistsSongs(currentPlayingSong!!.id).firstOrNull()
        } catch (e: Exception) {
            saveEmpty()
        }


        if (isActive) cancel()
    }

}