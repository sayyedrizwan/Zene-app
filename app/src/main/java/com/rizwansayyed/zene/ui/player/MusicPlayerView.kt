package com.rizwansayyed.zene.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.utils.Utils.toast

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicPlayerView(playerInfo: MusicPlayerData?, close: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { playerInfo?.list?.size ?: 0 })
    var name by remember { mutableStateOf("") }
    var artists by remember { mutableStateOf("") }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(MainColor)
    ) {
        item {
            Spacer(Modifier.height(60.dp))
            TextPoppins(stringResource(R.string.zene_music_player), true, size = 17)
            Spacer(Modifier.height(20.dp))
        }

        item {
            MusicListCards(pagerState, playerInfo, name, artists)
        }

        item {
            SongSliderData(playerInfo)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        name = playerInfo?.list?.get(pagerState.currentPage)?.name ?: ""
        artists = playerInfo?.list?.get(pagerState.currentPage)?.artists ?: ""
    }

    LaunchedEffect(Unit) {
        playerInfo?.list?.forEachIndexed { index, z ->
            if (z.id == playerInfo.player?.id) {
                pagerState.scrollToPage(index)
                name = z.name ?: ""
                artists = z.artists ?: ""
            }
        }
    }

    BackHandler {
        close()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicListCards(
    pagerState: PagerState, playerInfo: MusicPlayerData?, name: String, artists: String
) {
    Spacer(Modifier.height(60.dp))

    HorizontalPager(
        pagerState, contentPadding = PaddingValues(horizontal = 34.dp)
    ) { page ->
        Box(Modifier.fillMaxWidth()) {
            AsyncImage(
                playerInfo?.list?.get(page)?.thumbnail,
                playerInfo?.list?.get(page)?.name,
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )

            if (playerInfo?.player?.id != playerInfo?.list?.get(page)?.id)
                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(9.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MainColor)
                        .padding(8.dp)
                        .clickable {
                            sendWebViewCommand(playerInfo?.list?.get(page)!!, playerInfo.list)
                        }
                ) {
                    ImageIcon(R.drawable.ic_play, 24)
                }
        }
    }

    Spacer(Modifier.height(25.dp))
    TextPoppins(name, true, size = 22)
    Spacer(Modifier.height(5.dp))
    TextPoppinsThin(artists, true, size = 17)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongSliderData(playerInfo: MusicPlayerData?) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        Spacer(Modifier.width(6.dp))
        TextPoppins(playerInfo?.formatCurrentDuration() ?: "0:00", false, size = 16)
        Spacer(Modifier.width(6.dp))

        Slider(value = sliderPosition,
            onValueChange = { sliderPosition = it },
            Modifier.weight(1f),
            valueRange = 0f..(playerInfo?.totalDuration ?: 0).toFloat(),
            colors = SliderColors(
                activeTickColor = Color.White,
                activeTrackColor = Color.White,
                disabledActiveTickColor = Color.DarkGray,
                disabledInactiveTickColor = Color.DarkGray,
                disabledInactiveTrackColor = Color.DarkGray,
                disabledThumbColor = Color.DarkGray,
                inactiveTickColor = Color.DarkGray,
                inactiveTrackColor = Color.DarkGray,
                thumbColor = Color.White,
                disabledActiveTrackColor = Color.White,
            ),
            thumb = {
                Spacer(Modifier.padding(5.dp))
            },
            onValueChangeFinished = {
                sendWebViewCommand(SEEK_DURATION_VIDEO, sliderPosition.toInt())
            })

        Spacer(Modifier.width(6.dp))
        TextPoppins(playerInfo?.formatTotalDuration() ?: "0:00", false, size = 16)
        Spacer(Modifier.width(6.dp))
    }

    LaunchedEffect(playerInfo?.currentDuration) {
        sliderPosition = (playerInfo?.currentDuration ?: 0).toFloat()
    }
}