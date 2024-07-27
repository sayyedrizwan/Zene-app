package com.rizwansayyed.zene.ui.extra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.extra.mymusic.InfoOf
import com.rizwansayyed.zene.ui.extra.mymusic.MusicTopProfileView
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun MyMusicView(viewModel: ZeneViewModel, close: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val profile by userInfoDB.collectAsState(initial = null)

    AsyncImage(
        imgBuilder("https://lh3.googleusercontent.com/pw/AP1GczO9u5PwWEFojJNi6m_d8iEKQ3amKeL_4f50gZ4v6maQPmEi0e_hmFpMZHlQhrh8Pd0zMQKLmPaSX9H0G9mfnZUEJdMG-incZq_D9p91_iLYiel_UmL_rM0x4kR8C8BdSTeD7sbS83J10KfR5Hiya2OErw=w1220-h1626-s-no-gm"),
        profile?.name,
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentScale = ContentScale.Crop
    )

    LazyColumn(
        Modifier
            .padding(horizontal = 30.dp)
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        item {
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height((screenHeight.value / 1.5).dp)
            )
        }

        item {
            Spacer(
                Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 19.dp, topEnd = 19.dp))
                    .background(MainColor)
            )
        }

        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MainColor)
                    .padding(horizontal = 17.dp)
            ) {
                MusicTopProfileView(profile)
            }
        }

        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MainColor)
                    .padding(horizontal = 10.dp)
            ) {
                InfoOf(profile)
            }
        }

        item {
            HorizontalSongView(
                viewModel.songHistory, Pair(TextSize.SMALL, R.string.songs_history),
                StyleSize.SHOW_AUTHOR, showGrid = true
            )
        }

        item {
            Spacer(
                Modifier
                    .height(160.dp)
                    .fillMaxWidth()
                    .background(MainColor)
            )
        }
    }


    Row(
        Modifier
            .padding(top = 50.dp, start = 20.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(Color.Black)
            .clickable { close() }
            .padding(10.dp)
    ) {
        ImageIcon(id = R.drawable.ic_arrow_left, 29)
    }
}