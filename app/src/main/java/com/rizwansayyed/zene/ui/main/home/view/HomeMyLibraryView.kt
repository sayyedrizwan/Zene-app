package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.MusicHistoryResponse
import com.rizwansayyed.zene.data.model.MyLibraryTypes
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeMyLibraryView() {
    val viewModel: MyLibraryViewModel = hiltViewModel()
    var selectedType by remember { mutableStateOf(MyLibraryTypes.HISTORY) }
    var isLaunched by remember { mutableStateOf(false) }

    LazyColumn(Modifier.fillMaxSize()) {
        item { Spacer(Modifier.height(10.dp)) }

        stickyHeader {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(DarkCharcoal)
                    .padding(vertical = 5.dp)
            ) {
                MyLibraryTypes.entries.forEach {
                    ButtonWithBorder(
                        it.names, if (selectedType == it) Color.White else Color.DarkGray
                    ) {
                        selectedType = it
                    }
                }
            }
        }

        item { Spacer(Modifier.height(30.dp)) }
        items(viewModel.historyList) { HistoryCardItems(it) }

        item {
            if (viewModel.historyIsLoading) CircularLoadingView()
        }

        item {
            LaunchedEffect(Unit) {
                if (isLaunched) {
                    viewModel.songHistoryList(false)
                }
            }
        }

        item { Spacer(Modifier.height(300.dp)) }
    }

    LaunchedEffect(Unit) {
        viewModel.songHistoryList(true)
        delay(1.seconds)
        isLaunched = true
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HistoryCardItems(data: MusicHistoryResponse) {
    Row(Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Color.Black)
        .clickable { startMedia(data.asMusicData()) }
        .padding(10.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        GlideImage(
            data.thumbnail,
            data.name,
            Modifier
                .padding(horizontal = 5.dp)
                .height(90.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Fit
        )
        Column(
            Modifier
                .padding(horizontal = 4.dp)
                .weight(1f), Arrangement.Center, Alignment.Start
        ) {
            TextViewBold(data.name ?: "", 13, line = 2)
            if ((data.artists?.length ?: 0) > 3) Box(Modifier.offset(y = (-2).dp)) {
                TextViewNormal(data.artists!!, 12, line = 1)
            }
        }

        if (data.asMusicData().type() == MusicDataTypes.SONGS || data.asMusicData()
                .type() == MusicDataTypes.AI_MUSIC
        ) {
            ImageIcon(R.drawable.ic_music_note, 19)
        } else if (data.asMusicData().type() == MusicDataTypes.PODCAST) {
            ImageIcon(R.drawable.ic_podcast, 19)
        } else if (data.asMusicData().type() == MusicDataTypes.RADIO) {
            ImageIcon(R.drawable.ic_radio, 19)
        } else if (data.asMusicData().type() == MusicDataTypes.VIDEOS) {
            ImageIcon(R.drawable.ic_video_replay, 19)
        }

    }

//    Column(Modifier
//        .clickable { startMedia(data.asMusicData()) }
//        .padding(horizontal = 2.dp)
//        .padding(bottom = 25.dp)
//        .fillMaxWidth()) {
//
//        GlideImage(
//            data.thumbnail,
//            data.name,
//            Modifier
//                .fillMaxWidth()
//                .aspectRatio(1f)
//                .clip(RoundedCornerShape(14.dp)),
//            contentScale = ContentScale.Fit
//        )
//        Spacer(Modifier.height(4.dp))
//        TextViewBold(data.name ?: "", 14, line = 1)
//        Box(Modifier.offset(y = (-9).dp)) {
//            TextViewNormal(data.artists ?: "", 12, line = 1)
//        }
//    }
}