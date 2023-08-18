package com.rizwansayyed.zene.ui.home.musicplay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.presenter.model.MusicPlayerState
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.theme.Purple
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.Utils.msToSongDuration
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds


@Composable
fun MusicPlayerView(modifier: Modifier = Modifier, nav: HomeNavViewModel = hiltViewModel()) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current

    AnimatedVisibility(nav.showMusicPlayerView.value,
        enter = slideInVertically { with(density) { -40.dp.roundToPx() } } + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        Column(
            Modifier
                .height(((80 / 100.0) * screenHeight.value.toInt()).toInt().dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(Purple)
                .verticalScroll(rememberScrollState())
        ) {
            MusicPlayerCardView(nav)
        }
    }
}


@Composable
fun MusicPlayerCardView(nav: HomeNavViewModel = hiltViewModel()) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val musicPlayer by dataStoreManager.musicPlayerData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.musicPlayerData.first() })
    val coroutine = rememberCoroutineScope()

    var songPlayingDuration by remember { mutableStateOf<Long>(0) }

    var sliderValue by remember { mutableStateOf(0f) }
    var changedStarted by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth()) {
        AsyncImage(
            musicPlayer?.thumbnail,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 30.dp)
                .width(screenWidth - 150.dp)
                .height(screenWidth - 150.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
    }

    QuickSandBold(
        musicPlayer?.songName ?: "",
        Modifier
            .padding(10.dp)
            .fillMaxWidth(), size = 25
    )

    QuickSandLight(musicPlayer?.artists ?: "", Modifier.fillMaxWidth(), size = 17)

    if (musicPlayer?.state == MusicPlayerState.LOADING) {
        Row(
            modifier = Modifier
                .padding(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(Modifier.size(30.dp), Color.White)
        }
    } else {
        Slider(
            value = sliderValue,
            onValueChange = {
                changedStarted = true
                sliderValue = it
                val progress = "000000".toLong() + ((musicPlayer?.duration
                    ?: "000000".toLong()) - "000000".toLong()) * it / 100

                songPlayingDuration = progress.toLong()
            },
            onValueChangeFinished = {
                changedStarted = false

                nav.playerDuration(songPlayingDuration)
            },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.Black,
                inactiveTrackColor = Color.White
            ),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Row(Modifier.fillMaxWidth()) {
            QuickSandLight(
                msToSongDuration(songPlayingDuration),
                size = 15,
                modifier = Modifier
                    .offset(x = 0.dp, y = (-10).dp)
                    .padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            QuickSandLight(
                msToSongDuration(musicPlayer?.duration ?: 0),
                size = 16,
                modifier = Modifier
                    .offset(x = 0.dp, y = (-10).dp)
                    .padding(end = 10.dp)
            )

        }
    }

    LaunchedEffect(Unit) {
        if (musicPlayer?.state != MusicPlayerState.PLAYING) {
            songPlayingDuration = musicPlayer?.currentDuration ?: 0
        }
        songPlayingDuration = nav.getPlayerDuration()

        if (songPlayingDuration != "0".toLong() && musicPlayer?.duration != null) {
            sliderValue =
                ((songPlayingDuration - "000000".toLong()) * 100f) / (musicPlayer?.duration!! - "000000".toLong())
        }
    }

//    DisposableEffect(musicPlayer) {
//        coroutine.launch {
//            while (true) {
//                delay(1.seconds)
//                if (musicPlayer?.state == MusicPlayerState.PLAYING && !changedStarted) {
//                    songPlayingDuration = nav.getPlayerDuration()
//
//                    sliderValue =
//                        (songPlayingDuration - musicPlayer?.duration!!).toFloat() / (songPlayingDuration - musicPlayer?.duration!!)
//
//                }
//            }
//        }
//        onDispose {
//            coroutine.cancel()
//        }
//    }
}