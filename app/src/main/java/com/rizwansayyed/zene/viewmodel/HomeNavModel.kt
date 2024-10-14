package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.view.NavHomeMenu
import com.rizwansayyed.zene.utils.Utils.URLS.GOOGLE_DOCS_HEADER_ALERT_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class HomeNavModel : ViewModel() {

    var showMusicPlayer by mutableStateOf(false)
    var selectedMenuItems by mutableStateOf(NavHomeMenu.HOME)
    var topAlertHeader by mutableStateOf("")


    var lastAdRunTimestamp by mutableStateOf<Long?>(null)
    var showingWebViewAds by mutableStateOf(false)

    fun showMusicPlayer(b: Boolean) {
        showMusicPlayer = b
    }

    fun selectedMenuItems(n: NavHomeMenu) {
        selectedMenuItems = n
    }

    fun setAdsTs() {
        lastAdRunTimestamp = System.currentTimeMillis()
    }

    fun clearAdsTs() {
        lastAdRunTimestamp = null
    }

    fun webViewStatus() {
        showingWebViewAds = !showingWebViewAds
    }

    fun getAlertHeader() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val doc = Jsoup.connect(GOOGLE_DOCS_HEADER_ALERT_URL).get()
            val ogDescription = doc.select("meta[property=og:description]").first()?.attr("content")
            topAlertHeader = ogDescription ?: ""
        } catch (e: Exception) {
            e.message
        }
    }
}