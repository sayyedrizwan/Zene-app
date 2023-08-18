package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
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
import com.rizwansayyed.zene.utils.Algorithims
import com.rizwansayyed.zene.utils.Algorithims.extractSongSubTitles
import com.rizwansayyed.zene.utils.Algorithims.extractSongTitles
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.Utils.updateStatus
import kotlinx.coroutines.flow.flowOf


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopHeaderPager(header: Array<MusicsHeader>, search: (String, String) -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var songName by remember { mutableStateOf("") }
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState.currentPage) {
        try {
            songName = header[pagerState.currentPage].name ?: ""
        } catch (e: Exception) {
            e.message
        }
    }

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
                    size = 35
                )

                Spacer(modifier = Modifier.height(3.dp))

                QuickSandLight(extractSongSubTitles(songName))

                Spacer(modifier = Modifier.height(10.dp))

                RoundOutlineButtons(Icons.Default.PlayArrow, stringResource(id = R.string.play)) {
                    updateStatus(
                        header[pagerState.currentPage].thumbnail,
                        extractSongTitles(songName),
                        extractSongSubTitles(songName),
                        "", MusicPlayerState.LOADING
                    )
                    search(
                        extractSongTitles(songName), extractSongSubTitles(songName)
                    )
                }
            }
        }
    }
}