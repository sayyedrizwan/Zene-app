package com.rizwansayyed.zene.presenter.ui.musicplayer.view.playerwebview.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.service.player.utils.Utils.playOrPauseMedia
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


@SuppressLint("SetJavaScriptEnabled", "ViewConstructor")
class PlayerWebView(
    private val context: Activity,
    private val videoId: String,
    private val fullScreenView: MutableState<View?>,
    private val landscape: (Boolean) -> Unit
) : WebView(context) {

    var isVideoPlaying by mutableStateOf(true)
    var isVideoEnded by mutableStateOf(false)
    var isMuted by mutableStateOf(false)
    var showIcons by mutableStateOf(false)

    private val layoutParamSize =
        LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    private val clientUserAgent =
        "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/17E148"

    private val originURL = "https://www.youtube.com"


    private val chromeClient = object : WebChromeClient() {
        override fun getDefaultVideoPoster(): Bitmap? {
            return BitmapFactory.decodeResource(context.applicationContext.resources, 2130837573)
        }

        override fun onHideCustomView() {
            super.onHideCustomView()
            setInitialScale(1)
            landscape(false)
            (context.window.decorView as FrameLayout).removeView(fullScreenView.value)
            fullScreenView.value = null
        }

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            super.onShowCustomView(view, callback)
            if (fullScreenView.value != null) {
                onHideCustomView()
                return
            }
            setInitialScale(0)
            landscape(true)
            fullScreenView.value = view
            (context.window.decorView as FrameLayout)
                .addView(fullScreenView.value, FrameLayout.LayoutParams(-1, -1))
        }
    }

    init {
        layoutParams = layoutParamSize
        webViewClient = WebViewClient()
        webChromeClient = chromeClient
        isFocusable = true
        isFocusableInTouchMode = true
        setInitialScale(1)
        setBackgroundColor(MainColor.value.toInt())
        addJavascriptInterface(PlayerInterface(), "PlayerInterface")

        settings.apply {
            domStorageEnabled = true
            databaseEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            userAgentString = clientUserAgent
            mediaPlaybackRequiresUserGesture = false
            builtInZoomControls = false
            javaScriptEnabled = true
            allowFileAccess = true
            databaseEnabled = true
            domStorageEnabled = true
            setGeolocationEnabled(false)
        }
        loadUrl()
    }

    private fun exitFullScreen() {
        evaluateJavascript("exitFullScreen();", null)
    }

    fun enterFullScreen() {
        dispatchTouchEvent(
            MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                -1f,
                -1f,
                0
            )
        )
        evaluateJavascript("enterFullScreen();", null)
    }

    fun play() {
        evaluateJavascript("playVideo();", null)
    }

    fun pause() {
        evaluateJavascript("pauseVideo();", null)
    }

    fun mute() {
        evaluateJavascript("mute();", null)
    }

    fun unMute() {
        evaluateJavascript("unMute();", null)
    }

    private fun loadUrl() {
        val player =
            readHTMLFromUTF8File(resources.openRawResource(R.raw.youtube_iframe_player_mini))
                .replace("<VideoID>", videoId)

        loadDataWithBaseURL(originURL, player, "text/html", "UTF-8", null)
    }

    inner class PlayerInterface {
        @JavascriptInterface
        fun playerIsReadyAndPlaying() {
            showIcons = true
            playOrPauseMedia(false)
        }

        @JavascriptInterface
        fun isPlaying(b: Boolean) {
            isVideoEnded = false
            isVideoPlaying = b
        }

        @JavascriptInterface
        fun doEnded() {
            isVideoEnded = true
        }

        @JavascriptInterface
        fun muteStatus(b: Boolean) {
            isMuted = b
        }
    }

    private fun readHTMLFromUTF8File(inputStream: InputStream): String {
        inputStream.use {
            try {
                val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
                return bufferedReader.readLines().joinToString("\n")
            } catch (e: Exception) {
                return ""
            }
        }
    }

    fun buttonBackPressed() {
        exitFullScreen()
        chromeClient.onHideCustomView()
    }

}