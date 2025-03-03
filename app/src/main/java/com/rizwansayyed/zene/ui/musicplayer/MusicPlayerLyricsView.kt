package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay

@Composable
fun MusicPlayerLyricsView(viewModel: PlayerViewModel, currentDuration: String?) {
    when (val v = viewModel.playerLyrics) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> CircularLoadingView()
        is ResponseResult.Success -> {
            if ((v.data.syncedLyrics?.trim()?.length ?: 0) > 5) {
                MusicPlayerSyncedLyricsView(v.data.syncedLyrics, currentDuration)
            } else if ((v.data.plainLyrics?.trim()?.length ?: 0) > 5) {
                MusicPlayerPlainLyricsView(v.data.plainLyrics)
            }
        }
    }
}

@Composable
fun MusicPlayerPlainLyricsView(plainLyrics: String?) {
    val lyrics by remember { mutableStateOf(plainLyrics?.split("\n")) }

    LazyColumn(
        Modifier
            .height(400.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Black)
            .fillMaxSize()
            .animateContentSize(), contentPadding = PaddingValues(16.dp)
    ) {
        item { Spacer(Modifier.height(20.dp)) }

        item { TextViewBold(stringResource(R.string.lyrics), 30) }

        item { Spacer(Modifier.height(20.dp)) }

        items(lyrics ?: emptyList()) {
            TextViewBold(it, 17)
        }
        item { Spacer(Modifier.height(50.dp)) }
    }
}

@Composable
fun MusicPlayerSyncedLyricsView(syncedLyrics: String?, currentDuration: String?) {
    val lyrics = remember { parseLyrics(syncedLyrics ?: "") }
    val currentIndex = getCurrentLyricIndex(lyrics, currentDuration?.toFloatOrNull() ?: 0f)
    val listState = rememberLazyListState()
    var userScrolled by remember { mutableStateOf(false) }

    LazyColumn(
        Modifier
            .height(400.dp)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Black)
            .animateContentSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    userScrolled = true
                }
            }, listState, contentPadding = PaddingValues(16.dp)
    ) {
        item { Spacer(Modifier.height(20.dp)) }

        item { TextViewBold(stringResource(R.string.lyrics), 30) }

        item { Spacer(Modifier.height(10.dp)) }

        itemsIndexed(lyrics) { index, (ts, lyric) ->
            Box(Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { getPlayerS()?.seekTo(ts.toLong() + 1) }
                .padding(top = 40.dp)) {
                if (index < currentIndex) TextViewBold(
                    lyric.substringAfter("] "), 18, Color.White.copy(0.7f)
                ) else if (index == currentIndex) TextViewBold(
                    lyric.substringAfter("] "), 17
                )
                else TextViewNormal(
                    lyric.substringAfter("] "), 17, Color.Gray
                )
            }
        }

        item { Spacer(Modifier.height(50.dp)) }
    }

    LaunchedEffect(Unit) {
        delay(500)
        listState.animateScrollToItem(0)
    }

    LaunchedEffect(currentIndex) {
        if (!userScrolled && currentIndex != -1) {
            val centerIndex = maxOf(0, currentIndex + 1)
            listState.animateScrollToItem(centerIndex)
        }
    }
}

fun parseLyrics(lyricsText: String): List<Pair<Float, String>> {
    val regex = """\[(\d+):(\d+\.\d+)]\s*(.*)""".toRegex()
    return regex.findAll(lyricsText).map { match ->
        val minutes = match.groupValues[1].toFloat()
        val seconds = match.groupValues[2].toFloat()
        val timestamp = minutes * 60 + seconds
        val lyric = match.groupValues[3]
        timestamp to lyric
    }.toList()
}

fun getCurrentLyricIndex(lyrics: List<Pair<Float, String>>, currentTime: Float): Int {
    for (i in lyrics.indices) {
        if (i == lyrics.size - 1 || (lyrics[i].first <= currentTime && lyrics[i + 1].first > currentTime)) {
            return i
        }
    }
    return -1
}
