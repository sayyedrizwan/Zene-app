package com.rizwansayyed.zene.utils

import android.os.Build
import android.widget.Toast
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
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

}