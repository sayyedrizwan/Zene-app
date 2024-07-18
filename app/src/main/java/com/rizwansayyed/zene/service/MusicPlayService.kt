package com.rizwansayyed.zene.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.NEW_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.registerWebViewCommand
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


class MusicPlayService : Service() {
    private lateinit var webView: WebView
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        webView = WebViewService(applicationContext).apply {
            enable()
            addJavascriptInterface(JavaScriptInterface(), "ZeneListener")
            webViewClient = webViewClientObject
            webChromeClient = webViewChromeClientObject
        }

        registerWebViewCommand(applicationContext, listener)

        job = CoroutineScope(Dispatchers.IO).launch {
            delay(4.seconds)
            while (true) {
                Log.d("TAG", "onCreate: runnned on ")
                delay(500)
            }
        }
    }

    fun loadURL(vID: String) {
        val player =
            readHTMLFromUTF8File(resources.openRawResource(R.raw.yt_music_player))
                .replace("<<VideoID>>", vID)

        webView.loadDataWithBaseURL(YOUTUBE_URL, player, "text/html", "UTF-8", null)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private val listener = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return

            val data = i.getStringExtra(Intent.ACTION_MAIN) ?: return

            if (data.contains(NEW_VIDEO)) loadURL(data.substringAfter(NEW_VIDEO))
        }
    }

    inner class JavaScriptInterface {
        @JavascriptInterface
        fun playerState(v: Int) {
            "$v".toast()
        }
    }


    private val webViewChromeClientObject = object : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            return true
        }
    }


    private val webViewClientObject = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean = true
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        job = null
    }
}