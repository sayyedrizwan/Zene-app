package com.rizwansayyed.zene.utils

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.ui.theme.MainColor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object Utils {
    object URLS {
        const val PRIVACY_POLICY = "https://www.zenemusic.co/privacy-policy"

        const val BASE_URL_IP = "http://ip-api.com/"
        const val JSON_IP = "json"

        val BASE_URL =
            if (BuildConfig.DEBUG) "http://192.168.0.101:5173/-api-/" else "http://www.zenemusic.co/-api-/"
        const val ZENE_USER_API = "zuser"

    }

    val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    fun Any.toast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@toast.toString(), Toast.LENGTH_LONG).show()
    }

    fun openBrowser(url: String) = CoroutineScope(Dispatchers.Main).launch {
        try {
            val customTabsIntent = CustomTabsIntent.Builder()
            customTabsIntent.setUrlBarHidingEnabled(true)
            customTabsIntent.setShowTitle(true)

            customTabsIntent.setStartAnimations(
                context, android.R.anim.fade_in, android.R.anim.fade_out
            ).setExitAnimations(
                context, android.R.anim.slide_in_left, android.R.anim.slide_out_right
            )

            val build = customTabsIntent.build()
            build.intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            build.launchUrl(context, Uri.parse(url))
        } catch (e: Exception) {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            i.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }
}