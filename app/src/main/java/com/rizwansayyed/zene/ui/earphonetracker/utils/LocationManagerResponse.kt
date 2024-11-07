package com.rizwansayyed.zene.ui.earphonetracker.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.isGPSEnabled
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.isLocationDisabled
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume


@Singleton
class LocationManagerResponse @Inject constructor(@ApplicationContext val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    suspend fun locationUpdates() = suspendCancellableCoroutine { continuation ->
        if (isLocationDisabled()) {
            continuation.resume(LocationManagerData((-1).toDouble(), (-1).toDouble(), ""))
            return@suspendCancellableCoroutine
        }

        if (!isGPSEnabled()) {
            continuation.resume(LocationManagerData((-2).toDouble(), (-2).toDouble(), ""))
            return@suspendCancellableCoroutine
        }

        val token = object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                CancellationTokenSource().token

            override fun isCancellationRequested() = false
        }


        try {
            runBlocking(Dispatchers.IO) {
                val it = fusedLocationClient
                    .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, token).await()
                val address = addressInfo(it.latitude, it.longitude)
                continuation.resume(LocationManagerData(it.latitude, it.longitude, address))
            }
        } catch (e: Exception) {
            continuation.resume(LocationManagerData(0.0, 0.0, ""))
        }
    }

    private suspend fun addressInfo(lat: Double, lon: Double) =
        suspendCancellableCoroutine { continuation ->
            val listener = Geocoder.GeocodeListener {
                continuation.resume("${it.firstOrNull()?.subLocality}, ${it.firstOrNull()?.locality}")
            }
            Geocoder(context).getFromLocation(lat, lon, 1, listener)
        }
}

data class LocationManagerData(val lat: Double, val lon: Double, val address: String)