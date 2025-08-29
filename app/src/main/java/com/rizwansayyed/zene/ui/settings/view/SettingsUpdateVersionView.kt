package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.safeLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

@Composable
fun SettingsUpdateVersionView(modifier: Modifier = Modifier) {
    val packageName = LocalContext.current.applicationContext.packageName

//    LaunchedEffect(Unit) {
//        getLatestVersionFromPlayStore(packageName) {
//            it?.toast()
//        }
//    }
}

fun getLatestVersionFromPlayStore(packageName: String, onResult: (String?) -> Unit) {
    CoroutineScope(Dispatchers.IO).safeLaunch {
        try {
            val url = "https://play.google.com/store/apps/details?id=$packageName&hl=en&gl=us"

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url).header("User-Agent", "Mozilla/5.0").build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    onResult(null)
                    return@use
                }

                val html = response.body?.string() ?: ""
                val doc = Jsoup.parse(html)

                val version = doc.select("div.hAyfc")
                    .firstOrNull { it.select("div.BgcNfc").text() == "Current Version" }
                    ?.select("span.htlgb")
                    ?.getOrNull(0)
                    ?.text()

                onResult(version)
            }
        } catch (e: Exception) {
            e.message?.toast()
            onResult(null)
        }
    }.start()
}