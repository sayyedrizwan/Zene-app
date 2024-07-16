package com.rizwansayyed.zene.utils

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.ui.theme.MainColor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


object Utils {

    object IDs {
        const val AD_UNIT_ID: String = "ca-app-pub-2941808068005217/7650500204"
        val AD_BANNER_ID: String =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9214589741" else "ca-app-pub-2941808068005217/1624020934"
    }

    object URLS {
        const val PRIVACY_POLICY = "https://www.zenemusic.co/privacy-policy"

        const val YOUTUBE_MUSIC = "https://www.youtube.com/music"


        const val BASE_URL_IP = "http://ip-api.com/"
        const val JSON_IP = "json"

        val BASE_URL =
            if (BuildConfig.DEBUG) "http://192.168.0.101:5173/-api-/" else "http://www.zenemusic.co/-api-/"
        const val ZENE_USER_API = "zuser"
        const val ZENE_MOODS_API = "moods"
        const val ZENE_NEW_RELEASE_API = "newrelease"
        const val ZENE_SUGGESTED_SONGS_API = "suggestedsongs"
        const val ZENE_TOP_GLOBAL_ARTISTS_API = "top/globalartists"
        const val ZENE_TOP_LISTEN_SONGS_API = "top/listensongs"
        const val ZENE_TOP_ARTISTS_API = "top/artistssongs"
        const val ZENE_TOP_PLAYLISTS_API = "top/playlists"
        const val ZENE_TOP_ALBUMS_API = "top/albums"
        const val ZENE_TOP_VIDEOS_API = "top/videos"
        const val ZENE_TOP_SONGS_API = "top/songs"


        const val USER_AGENT_D =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"

    }

    const val TOTAL_GRID_SIZE = 12
    const val TWO_GRID_SIZE = 6
    const val THREE_GRID_SIZE = 4


    val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    fun Any.toast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@toast.toString(), Toast.LENGTH_LONG).show()
    }

    fun ytThumbnail(id: String): String {
        return "https://i.ytimg.com/vi/${id}/maxresdefault.jpg"
    }

    fun convertItToMoney(s: String): String {
        return try {
            val m = s.toInt()
            return DecimalFormat("#,###").format(m)
        } catch (e: Exception) {
            s
        }
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