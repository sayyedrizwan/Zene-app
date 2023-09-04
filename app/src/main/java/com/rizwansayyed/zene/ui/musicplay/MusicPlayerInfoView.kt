package com.rizwansayyed.zene.ui.musicplay

import android.content.Intent
import android.net.Uri
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.presenter.jsoup.WikipediaInfoManager
import com.rizwansayyed.zene.ui.theme.WhiteLight
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandLightUnderline
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.Utils.URL.WIKIPEDIA_DONATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun MusicPlayerInfo() {
    val musicPlayer by BaseApplication.dataStoreManager.musicPlayerData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { BaseApplication.dataStoreManager.musicPlayerData.first() })

    val context = LocalContext.current.applicationContext

    var showLoading by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var gUrl by remember { mutableStateOf("") }

    if (showLoading)
        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            CircularProgressIndicator(Modifier.padding(20.dp).size(30.dp), Color.White)
        }
    else
        if (text.isEmpty())
            QuickSandLight(
                stringResource(id = R.string.no_info_found_for_song),
                size = 12, modifier = Modifier.padding(20.dp)
            )
        else {
            AndroidView(
                modifier = Modifier.padding(13.dp),
                factory = { context ->
                    TextView(context).apply {
                        autoLinkMask = Linkify.WEB_URLS
                        linksClickable = true
                        setLinkTextColor(Color.White.toArgb())
                        setTextColor(Color.White.toArgb())
                        textSize = 16f
                    }
                },
                update = {
                    it.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
                }
            )

            Row(
                Modifier
                    .padding(vertical = 30.dp)
                    .fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                Image(
                    painterResource(id = R.drawable.ic_wikipedia),
                    "",
                    Modifier.size(35.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Spacer(Modifier.width(8.dp))

                Column {
                    QuickSandLightUnderline(
                        stringResource(id = R.string.full_article_on_wikipedia),
                        size = 15, modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(gUrl)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(this)
                                }
                            }
                    )

                    QuickSandLight(
                        stringResource(id = R.string.donate_on_wikipedia),
                        size = 15, modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(WIKIPEDIA_DONATE)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(this)
                                }
                            }
                    )
                }
            }
        }


    LaunchedEffect(Unit) {
        showLoading = true
        WikipediaInfoManager(musicPlayer?.songName!!, musicPlayer?.artists!!) { data, url ->
            text = data
            gUrl = url
            showLoading = false
        }
    }

}