package com.rizwansayyed.zene.ui.home.homeui

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.model.MusicPlayerDetails
import com.rizwansayyed.zene.presenter.model.MusicPlayerState
import com.rizwansayyed.zene.presenter.model.MusicsHeader
import com.rizwansayyed.zene.ui.BlackShade
import com.rizwansayyed.zene.ui.RoundOutlineButtons
import com.rizwansayyed.zene.ui.theme.Purple
import com.rizwansayyed.zene.utils.Algorithims
import com.rizwansayyed.zene.utils.Algorithims.extractSongSubTitles
import com.rizwansayyed.zene.utils.Algorithims.extractSongTitles
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.Utils.updateStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopHeaderPager(header: Array<MusicsHeader>, search: (String, String, String) -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var autoScrollJob by remember { mutableStateOf<Job?>(null) }
    val coroutine = rememberCoroutineScope()


    var songName by remember { mutableStateOf("") }
    val pagerState = rememberPagerState()

    Column {
        HorizontalPager(pageCount = header.size, state = pagerState) {
            AsyncImage(
                model = header[it].thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2)
            )
        }

        Box {
            BlackShade()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(25.dp))

                QuickSandBold(
                    extractSongTitles(songName),
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth(),
                    size = 35,
                    maxLine = 1
                )

                Spacer(modifier = Modifier.height(3.dp))

                QuickSandLight(extractSongSubTitles(songName))

                Spacer(modifier = Modifier.height(10.dp))

                RoundOutlineButtons(Icons.Default.PlayArrow, stringResource(id = R.string.play)) {
                    updateStatus(
                        header[pagerState.currentPage].thumbnail, extractSongTitles(songName),
                        extractSongSubTitles(songName), "", MusicPlayerState.LOADING
                    )
                    search("", songName, extractSongSubTitles(songName))
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
            repeat(header.size) {
                if (pagerState.currentPage == it)
                    DotsSliderView(Purple)
                else
                    DotsSliderView(Color.White)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            autoScrollJob?.cancel()
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        autoScrollJob?.cancel()

        try {
            songName = header[pagerState.currentPage].name ?: ""
        } catch (e: Exception) {
            e.message
        }

        autoScrollJob = coroutine.launch {
            while (true) {
                delay(3.seconds)
                if (pagerState.canScrollForward)
                    pagerState.scrollToPage(pagerState.currentPage + 1)
                else
                    pagerState.scrollToPage(0)
            }
        }
    }
}

@Composable
fun BetaTestDialog() {

    val alert by dataStoreManager.betaDialog.collectAsState(initial = false)

    if (alert) AlertDialog(
        onDismissRequest = { dataStoreManager.betaDialog = flowOf(false) },
        text = {
            Column(Modifier.fillMaxWidth(), Arrangement.Center) {
                QuickSandBold(
                    stringResource(id = R.string.zene_is_beta),
                    size = 15,
                    modifier = Modifier.padding(top = 10.dp),
                    color = Color.Black
                )

                QuickSandRegular(
                    stringResource(id = R.string.zene_is_beta_desc),
                    size = 16,
                    modifier = Modifier.padding(top = 10.dp),
                    color = Color.Black,
                    align = TextAlign.Start
                )
            }
        },
        dismissButton = { Text(text = "") },
        confirmButton = {
            Text(text = stringResource(id = R.string.close), modifier = Modifier.clickable {
                dataStoreManager.betaDialog = flowOf(false)
            })
        }
    )
}

@Composable
fun DotsSliderView(color: Color = Color.White) {
    Spacer(
        Modifier
            .animateContentSize()
            .padding(1.dp)
            .clip(RoundedCornerShape(100))
            .width(10.dp)
            .height(2.dp)
            .background(color)
    )
}