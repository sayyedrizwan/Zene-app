package com.rizwansayyed.zene.ui.home.musicplay

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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
                .height(((95 / 100.0) * screenHeight.value.toInt()).toInt().dp)
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