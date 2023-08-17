package com.rizwansayyed.zene

import android.app.Application
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.rizwansayyed.zene.datastore.DataStoreManager
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerService
import com.rizwansayyed.zene.ui.home.MainActivity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

//reset password lock completed yesterday received API
//digital tag bugs is fixed completed for madhur
//dolphin tracker crash fixed
//started tracking feature. completed button and working on connecting.


//add info about of tracker and qr code.

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        @Volatile
        lateinit var context: BaseApplication

        @Volatile
        lateinit var dataStoreManager: DataStoreManager
    }

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


//        CoroutineScope(Dispatchers.IO).launch {
//            delay(2.seconds)
//            Intent(context, MediaPlayerService::class.java).also {
//                context.startService(it)
//            }
//        }
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
}