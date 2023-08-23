package com.rizwansayyed.zene.ui.musicplay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerViewStatus.*
import com.rizwansayyed.zene.ui.theme.Purple


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
                .height(((95 / 100.0) * screenHeight.value.toInt()).toInt().dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(Purple)
                .verticalScroll(rememberScrollState())
        ) {
            when(nav.musicViewType.value){
                MUSIC ->  MusicPlayerCardView(nav)
                LYRICS -> MusicPlayerLyrics(nav)
                INSTAGRAM -> MusicPlayerLyrics(nav)
            }

            Spacer(modifier = Modifier.height(190.dp))
        }
    }

    LaunchedEffect(nav.showMusicPlayerView.value) {
        nav.musicViewType(MUSIC)
    }
}


@Composable
fun PlayerImgIcon(ic: Int, color: Color = Color.White, click: () -> Unit) {
    Image(
        painterResource(id = ic),
        "",
        Modifier
            .padding(15.dp)
            .size(35.dp)
            .clickable {
                click()
            },
        colorFilter = ColorFilter.tint(color = color)
    )
}

fun sliderDurationValue(songPlayingDuration: Long, duration: Long): Float {
    return ((songPlayingDuration - "000000".toLong()) * 100f) / (duration - "000000".toLong())
}