package com.rizwansayyed.zene.ui.musicplay

import android.widget.TextView
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.presenter.jsoup.WikipediaInfoManager
import com.rizwansayyed.zene.utils.QuickSandRegular
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun MusicPlayerInfo() {
    val musicPlayer by BaseApplication.dataStoreManager.musicPlayerData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { BaseApplication.dataStoreManager.musicPlayerData.first() })

    var showLoading by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    AndroidView(
        modifier = Modifier,
        factory = { context ->
            TextView(context)
        },
        update = {
            it.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )

//    QuickSandRegular(
//        text,
//        size = 14,
//        modifier = Modifier.padding(14.dp)
//    )

    LaunchedEffect(Unit) {
        showLoading = true
        WikipediaInfoManager(musicPlayer?.songName!!, musicPlayer?.artists!!) {
            text = it
            showLoading = false
        }
    }

}