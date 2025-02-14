package com.rizwansayyed.zene.ui.videoplayer.view

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.rizwansayyed.zene.datastore.DataStorageManager.videoCCDB
import com.rizwansayyed.zene.ui.main.search.view.removeYoutubeTopView
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.utils.WebViewUtils.enable
import com.rizwansayyed.zene.viewmodel.PlayingVideoInfoViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("ClickableViewAccessibility")
@Composable
fun VideoPlayerVideoView(
    modifier: Modifier, videoID: String?, viewModel: PlayingVideoInfoViewModel
) {

    var didRemoved by remember { mutableStateOf(false) }
    var job by remember { mutableStateOf<Job?>(null) }

    val coroutine = rememberCoroutineScope()

    class WebAppInterface {
        @JavascriptInterface
        fun videoState(playerState: Int, currentTS: String, duration: String, isMuted: Boolean) {
            viewModel.setVideoState(playerState, currentTS, duration, isMuted)
        }

        @JavascriptInterface
        fun videoInfo(title: String, author: String) {
            viewModel.setVideoInfo(title, author)
            coroutine.launch {
                if (videoCCDB.first())
                    viewModel.webView?.evaluateJavascript("enableCaption()", null)
                else
                    viewModel.webView?.evaluateJavascript("disableCaption()", null)
            }
        }
    }

    AndroidView({ ctx ->
        WebView(ctx).apply {
            enable()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    if (view.progress == 100) removeYoutubeTopView(view) {
                        didRemoved = true
                    }
                }
            }

            setOnTouchListener { v, m ->
                if (m.action == MotionEvent.ACTION_UP) {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    viewModel.showControlView(true)
                } else v.parent.requestDisallowInterceptTouchEvent(true)

                false
            }

            addJavascriptInterface(WebAppInterface(), "Zene")
            settings.setSupportZoom(true)
            settings.builtInZoomControls = false
            viewModel.setWebViewTo(this@apply)
            viewModel.setVideoThumb(videoID)
            viewModel.loadWebView()
        }
    }, modifier.fillMaxSize())

    AnimatedVisibility(viewModel.showControlView, Modifier, fadeIn(), fadeOut()) {
        VideoPlayerControlView(viewModel)
    }

    if (!didRemoved) Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black), Alignment.Center
    ) {
        CircularLoadingView()
    }

    LifecycleResumeEffect(Unit) {
        job?.cancel()
        job = coroutine.launch {
            while (true) {
                viewModel.webView?.evaluateJavascript("playingStatus();", null)
                delay(500)
            }
        }

        onPauseOrDispose { job?.cancel() }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.killWebView()
        }
    }
}