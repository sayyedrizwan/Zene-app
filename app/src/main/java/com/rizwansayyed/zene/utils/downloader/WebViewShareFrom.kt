package com.rizwansayyed.zene.utils.downloader

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.URL.SAVE_FROM_NET
import com.rizwansayyed.zene.utils.Utils.URL.TEN_DOWNLOADER
import com.rizwansayyed.zene.utils.Utils.URL.videoPaths
import com.rizwansayyed.zene.utils.Utils.URL.videoPaths10
import com.rizwansayyed.zene.utils.Utils.enableAll
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@SuppressLint("ViewConstructor")
class WebViewShareFrom(
    context: Context,
    private val vID: String,
    private val download: (String) -> Unit
) : WebView(context) {

    private val scope = MainScope()
    private var job: Job? = null

    private var attempts = 0
    private var doFinishScriptRun = false
    private var didRetried = false

    private val runScriptTag = """
        document.querySelector('.link.link-download.subname.ga_track_events.download-icon').click();
    """

    private val runTenScriptTag = """
       const downloadBtn = document.querySelectorAll('.downloadBtn');
       if (downloadBtn.length > 0) {
          downloadBtn[0].click();
       }
    """

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (!doFinishScriptRun) {
                if (url?.contains(SAVE_FROM_NET) == true) {
                    evaluateJavascript(runScriptTag) {}
                    startRunningLoop()
                } else if (url?.contains(TEN_DOWNLOADER) == true) {
                    evaluateJavascript(runTenScriptTag) {}
                    startRunningLoop()
                }
            }
            doFinishScriptRun = true
        }

        override fun onReceivedHttpError(
            view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?
        ) {
            if (url?.contains(SAVE_FROM_NET) == true) {
                loadOtherWebsite()
            } else if (url?.contains(TEN_DOWNLOADER) == true) {
                download("non")
            }
        }

        override fun onReceivedError(
            view: WebView?, request: WebResourceRequest?, error: WebResourceError?
        ) {
            if (url?.contains(SAVE_FROM_NET) == true) {
                loadOtherWebsite()
            } else if (url?.contains(TEN_DOWNLOADER) == true) {
                download("non")
            }
        }
    }

    private fun startRunningLoop() {
        job = scope.launch {
            while (true) {
                delay(3.seconds)

                if (attempts >= 3) {
                    if (didRetried)
                        download("non")
                    else
                        loadOtherWebsite()
                }
                attempts += 1

                if (url?.contains(SAVE_FROM_NET) == true) {
                    evaluateJavascript(runScriptTag) {}
                } else if (url?.contains(TEN_DOWNLOADER) == true) {
                    evaluateJavascript(runTenScriptTag) {}
                }
            }
        }
    }

    init {
        enableAll(webViewClient)
        setDownloadListener { downloadURL, _, _, _, _ ->
            job?.cancel()
            job = null

            download(downloadURL)
        }

        if (vID.length > 3) {
            loadUrl(videoPaths(vID))
        }
    }

    private fun loadOtherWebsite() {
        if (vID.length <= 3) {
            download("non")
            return
        }
        job?.cancel()
        job = null
        didRetried = true
        doFinishScriptRun = false
        attempts = 0
        loadUrl(videoPaths10(vID))
    }
}