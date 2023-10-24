package com.rizwansayyed.zene.presenter.ui.musicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun MusicDialog() {
    val homeNavModel: HomeNavViewModel = hiltViewModel()
    val width = (LocalConfiguration.current.screenHeightDp / 1.5).dp

    Box(
        Modifier
            .fillMaxSize()
            .background(BlackColor.copy(0.6f))
    ) {
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(width)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(BlackColor)
        ) {

        }
    }

}