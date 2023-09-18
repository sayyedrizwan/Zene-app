package com.rizwansayyed.zene.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context


object Utils {
    fun isInternetConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null
    }
}