package com.rizwansayyed.zene.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.ForegroundService
import com.rizwansayyed.zene.ui.main.MainActivity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale


object MainUtils {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()!!

    fun Any.toast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@toast.toString(), Toast.LENGTH_LONG).show()
        if (isActive) cancel()
    }

    fun getDeviceInfo(): String {
        val manufacturer = Build.MANUFACTURER.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
        val model = Build.MODEL
        val osVersion = Build.VERSION.RELEASE
        return "$manufacturer $model $osVersion"
    }

    fun isLocationPermissionGranted(): Boolean {
        val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        else
            checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

        return location == PackageManager.PERMISSION_GRANTED
    }

    fun startAppService(context: Context) {
        Intent(context, ForegroundService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(this)
            else
                context.startService(this)
        }
    }

}