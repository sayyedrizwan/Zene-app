package com.rizwansayyed.zene

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.seconds

@Composable
fun TestWebView() {
    val context = LocalContext.current.applicationContext
    var isDialog by remember { mutableStateOf(false) }

    if (isDialog) Dialog(
        { isDialog = false },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column {
            AndroidView(factory = { ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = true
                    settings.loadsImagesAutomatically = true
                    settings.domStorageEnabled = true
                    settings.blockNetworkLoads = false
                    settings.databaseEnabled = true
                    settings.pluginState = WebSettings.PluginState.ON
                    settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                    settings.allowFileAccess = true

                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3.seconds)
                        evaluateJavascript("document.getElementById('sf_url').value = \"https://www.youtube.com/watch?v=q3zqJs7JUCQ\"; document.getElementById('sf_form').submit();\n") {}
                    }

                    loadUrl("https://en1.savefrom.net/2ol/")
                }
            }, modifier = Modifier.fillMaxSize())
        }
    }

    LaunchedEffect(Unit) {
        delay(2.seconds)
        isDialog = true
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun saveHtmlToLocalFile(context: Context, fileName: String, data: String) {
    val file = File(context.filesDir, fileName)
    file.writeText(data)
}