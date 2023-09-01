package com.rizwansayyed.zene

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.hilt.work.HiltWorkerFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.work.Configuration
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.rizwansayyed.zene.domain.datastore.DataStoreManager
import com.rizwansayyed.zene.presenter.model.MusicPlayerState
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerService
import com.rizwansayyed.zene.ui.home.MainActivity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

//reset password lock completed yesterday received API
//digital tag bugs is fixed completed for madhur
//dolphin tracker crash fixed
//started tracking feature. completed button and working on connecting.


// working on connecting users and background location

//add info about of tracker and qr code.

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {

    companion object {
        @Volatile
        lateinit var context: BaseApplication

        @Volatile
        lateinit var dataStoreManager: DataStoreManager
    }


    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate() {
        super.onCreate()
        context = this
        connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager

        dataStoreManager = DataStoreManager()

        connectivityManager.requestNetwork(networkRequest, networkCallback)

        CoroutineScope(Dispatchers.IO).launch {
            delay(1.seconds)
            val music = dataStoreManager.musicPlayerData.first()
            music?.state = MusicPlayerState.PAUSE
            dataStoreManager.musicPlayerData = flowOf(music)
        }
    }


    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            try {
                MainActivity.networkCallbackStatus.internetConnected()
            } catch (e: Exception) {
                e.message
            }
        }
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory).build()
}