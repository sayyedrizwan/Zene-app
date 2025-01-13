package com.rizwansayyed.zene.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.ForegroundService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
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
        val location = checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        return location == PackageManager.PERMISSION_GRANTED
    }

    fun startAppService(context: Context) {
        Intent(context, ForegroundService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(this)
            else context.startService(this)
        }
    }

    fun loadIcon(context: Context, url: String?, placeHolder: Int): BitmapDescriptor? {
        try {
            var bitmap: Bitmap? = null
            Glide.with(context).asBitmap().load(url).error(placeHolder)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) {
                        bitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
            return BitmapDescriptorFactory.fromBitmap(bitmap!!)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun getBitmapFromURL(strURL: String?): Bitmap? = runBlocking(Dispatchers.IO) {
        try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            return@runBlocking myBitmap
        } catch (e: IOException) {
            val icon = BitmapFactory.decodeResource(context.resources, R.drawable.zusers_img)
            return@runBlocking icon
        }
    }
}