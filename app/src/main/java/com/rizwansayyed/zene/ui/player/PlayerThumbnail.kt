package com.rizwansayyed.zene.ui.player

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.Utils.getPercentageLeft
import kotlinx.coroutines.delay

@Composable
fun PlayerThumbnail(modifier: Modifier = Modifier) {
    val isBig = isScreenBig()
    val info by musicPlayerDB.collectAsState(initial = null)
    var currentProgress by remember { mutableFloatStateOf(0f) }

    Box(
        modifier
            .padding(bottom = if (isBig) 90.dp else 50.dp, end = 10.dp)
            .size(if (isBig) 125.dp else 90.dp)
            .background(Color.Transparent)
            .clickable { }) {

        AsyncImage(
            info?.player?.thumbnail,
            info?.player?.name,
            Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        )

        LinearProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            trackColor = Color.Transparent, color = MainColor
        )

        if (info?.isBuffering == true){
            LoadingView()
        }
    }


    LaunchedEffect(info?.currentDuration) {
        currentProgress = if (info?.currentDuration == 0) 0f
        else getPercentageLeft(info?.currentDuration, info?.totalDuration).toFloat() / 100
    }
}