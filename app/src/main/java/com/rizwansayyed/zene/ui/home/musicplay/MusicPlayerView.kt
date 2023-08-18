package com.rizwansayyed.zene.ui.home.musicplay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.presenter.model.MusicPlayerDetails
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.theme.Purple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


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
                .fillMaxWidth()
                .height(screenHeight / 2 + 150.dp)
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

    val musicPlayer by dataStoreManager.musicPlayerData.collectAsState(initial = null)

    Box(Modifier.fillMaxWidth()) {
        AsyncImage(
            "https://i.scdn.co/image/ab67616d00001e02f5dc36d5000145375a41c3b8",
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

}