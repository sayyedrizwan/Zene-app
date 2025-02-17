package com.rizwansayyed.zene.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import kotlinx.coroutines.tasks.await
import kotlin.time.Duration.Companion.minutes

@SuppressLint("MissingPermission")
class BackgroundLocationTracking(context: Context) : LocationTrackingBackground {
    companion object {
        var updateLocationLat = 0.0
        var updateLocationLon = 0.0
        var backgroundTracking: LocationTrackingBackground? = null

        suspend fun getLatestLocation(): Location? {
            val locationClient = LocationServices.getFusedLocationProviderClient(context)

            return runCatching {
                val token = CancellationTokenSource().token
                locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token).await()
            }.getOrElse {
                runCatching { locationClient.lastLocation.await() }.getOrNull()
            }
        }
    }

    init {
        backgroundTracking = this
    }

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (location in p0.locations) {
                updateLocationLat = location.latitude
                updateLocationLon = location.longitude
            }
        }
    }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun start() {
        try {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 2.minutes.inWholeMilliseconds
            ).setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1.minutes.inWholeMilliseconds)
                .setMaxUpdateDelayMillis(3.minutes.inWholeMilliseconds).build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDataReceived() {
        start()
    }
}