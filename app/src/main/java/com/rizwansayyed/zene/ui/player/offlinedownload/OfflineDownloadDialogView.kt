package com.rizwansayyed.zene.ui.player.offlinedownload

import android.graphics.Color
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIHttpService.downloadAFile
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SmallButtonBorderText
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.viewmodel.RoomDBViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

fun jsRunValue(id: String?): String {
    return """
    document.getElementById('sf_url').value = "https://www.youtube.com/watch?v=${id}"; document.getElementById('sf_form').submit();
"""
}

@Composable
fun OfflineDownload(m: ZeneMusicDataItems, close: () -> Unit) {
    val roomViewModel: RoomDBViewModel = hiltViewModel()
    val coroutine = rememberCoroutineScope()

    var downloadLink by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Dialog(close, DialogProperties(false, dismissOnClickOutside = false)) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .height(300.dp)
        ) {
            AndroidView(factory = { ctx ->
                WebView(ctx).apply {
                    enable()
                    class JavaScriptInterface {
                        @JavascriptInterface
                        fun onDownloadLinkDetected(href: String) {
                            if (downloadLink != href) downloadLink = href
                        }

                        @JavascriptInterface
                        fun onError() {
                            error = true
                        }
                    }
                    setBackgroundColor(Color.TRANSPARENT)
                    addJavascriptInterface(JavaScriptInterface(), "Interface")

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            if (newProgress == 100) CoroutineScope(Dispatchers.Main).launch {
                                delay(1.seconds)
                                injectJavaScript(this@apply)
                                delay(2.seconds)
                                evaluateJavascript(jsRunValue(m.id)) {}
                            }
                        }
                    }
                    loadUrl("https://en1.savefrom.net/2ol/")
                }
            }, modifier = Modifier.size(50.dp))

            Column(
                Modifier
                    .fillMaxSize()
                    .background(MainColor)
                    .padding(8.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                if (error) {
                    TextPoppins(stringResource(R.string.cache_error), true, size = 15)

                    SmallButtonBorderText(R.string.close) {
                        close()
                    }
                } else {
                    TextPoppins(
                        stringResource(R.string.offline_caching_availability), true, size = 15
                    )

                    Spacer(Modifier.height(30.dp))

                    LoadingView(Modifier.size(34.dp))

                    Spacer(Modifier.height(30.dp))

                    SmallButtonBorderText(R.string.cancel) {
                        close()
                    }
                }
            }

            LaunchedEffect(downloadLink) {
                if (downloadLink.length > 4 && downloadLink.contains("https://")) {
                    m.id?.let {
                        downloadAFile(downloadLink, it) { v ->
                            if (v) {
                                roomViewModel.saveOfflineSongs(m)
                                coroutine.launch {
                                    delay(1.seconds)
                                    close()
                                }
                            } else error = true
                        }
                    } ?: run {
                        error = true
                    }
                }
            }
        }
    }
}

private fun injectJavaScript(webView: WebView) {
    val jsCode = """
            var observer = new MutationObserver(function(mutations) {
                mutations.forEach(function(mutation) {
                    var downloadLink = document.querySelector('.def-btn-box a.link-download');
                    var errorMessage = document.querySelector('.result-box.simple.center.result-failure');
                    
                    if (downloadLink) {
                        observer.disconnect();
                        Interface.onDownloadLinkDetected(downloadLink.href);
                    } else if (errorMessage) {
                        observer.disconnect();
                        Interface.onError();
                    }
                });
            });
            observer.observe(document.body, { childList: true, subtree: true });
        """

    webView.evaluateJavascript(jsCode, null)
}