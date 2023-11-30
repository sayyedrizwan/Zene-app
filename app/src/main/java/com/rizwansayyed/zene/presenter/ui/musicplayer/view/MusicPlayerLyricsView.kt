package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.Pink80
import com.rizwansayyed.zene.presenter.theme.Purple40
import com.rizwansayyed.zene.presenter.theme.PurpleGrey80
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.formatExoplayerDuration
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicPlayerLyrics(playerViewModel: PlayerViewModel, player: ExoPlayer) {
    val coroutine = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    val text = remember { mutableStateListOf("") }
    var currentTextPosition by remember { mutableIntStateOf(0) }


    var isSubtitles by remember { mutableStateOf(false) }
    var pagerState = rememberPagerState(pageCount = { 0 })

    if (playerViewModel.lyricsInfo != null) {
        if (text.size == 0) {
            Spacer(Modifier.height(50.dp))
            TextThin(stringResource(R.string.no_lyrics_found))
            Spacer(Modifier.height(50.dp))
        } else {
            Spacer(Modifier.height(50.dp))

            if (isSubtitles) {
                pagerState = rememberPagerState(pageCount = { text.size })

                VerticalPager(
                    pagerState,
                    Modifier
                        .padding(9.dp)
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Purple40),
                    contentPadding = PaddingValues(vertical = 120.dp),
                ) { page ->
                    if (page == 0)
                        TextSemiBold(
                            stringResource(R.string.lyrics),
                            Modifier.padding(horizontal = 7.dp),
                            size = 25
                        )

                    TextSemiBold(
                        text[page], Modifier.padding(horizontal = 7.dp), size = 20,
                        color = if (page < currentTextPosition) Color.White else Color.Gray
                    )
                }

            } else
                Column(
                    Modifier
                        .padding(9.dp)
                        .fillMaxWidth()
                        .height(320.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Purple40)
                        .verticalScroll(rememberScrollState())
                ) {

                    Spacer(Modifier.height(30.dp))

                    TextSemiBold(
                        stringResource(R.string.lyrics),
                        Modifier.padding(horizontal = 7.dp),
                        size = 20
                    )

                    text.forEach {
                        TextSemiBold(
                            it,
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 7.dp), size = 20
                        )
                    }
                }
            Spacer(Modifier.height(50.dp))
        }
    } else {
        Spacer(Modifier.height(50.dp))
        Spacer(
            Modifier
                .padding(9.dp)
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(shimmerBrush())
        )
        Spacer(Modifier.height(50.dp))
    }


    DisposableEffect(Unit) {
        job = coroutine.launch {
            while (true) {
                val v = playerViewModel.lyricsInfo
                val currentTime = formatExoplayerDuration(player.currentPosition)

                if (v != null) {
                    val tempList = mutableListOf<String>()

                    if (v.subtitles) {
                        v.lyrics.split("[").forEachIndexed { i, s ->
                            if (s.contains(currentTime)) {
                                if (i != currentTextPosition) {
                                    currentTextPosition = i
//                                    pagerState.scrollToPage(i)
                                }
                            }
                        }
                    }

                    v.lyrics.split("[").forEach {
                        tempList.add(it.substringAfter("]").trim())
                        if (!v.subtitles) tempList.add("\n")
                    }

                    if (text != tempList) {
                        text.clear()
                        text.addAll(tempList.toList())
                        isSubtitles = v.subtitles
                    }
                }
//                val srt = parseSrt(v?.lyrics ?: "")
//                Log.d("TAG", "MusicPlayerLyrics: data $srt")
//                srt.forEach { data ->
//                    Log.d("TAG", "MusicPlayerLyrics: data $data")
//                }

//
//                if (v?.lyrics?.isNotEmpty() == true && v.subtitles) {
//                    text = " "
//                    val currentTime = formatExoplayerDuration(player.currentPosition)
//                    if (!v.lyrics.substringAfter("[$currentTime").substringAfter("[")
//                            .substringBefore("]").contains("00:00")
//                    ) {
//                        text = v.lyrics.substringAfter("[$currentTime").substringAfter("]")
//                            .substringBefore("[")
//                    }
//                } else if (v?.lyrics?.isNotEmpty() == true){
//                    text = v.lyrics.replace("[", "\n[")
//                }

                delay(1.seconds)
            }
        }
        onDispose {
            job?.cancel()
        }
    }
}


fun parseSrt(srtString: String): List<Subtitle> {
    val subtitles = mutableListOf<Subtitle>()

    val subtitleEntries = srtString.trim().split("\n\n")

    for (entry in subtitleEntries) {
        val lines = entry.split("\n")
        if (lines.size >= 3) {
            // Parse timestamp
            val timestamp = lines[1].split(" --> ")
            val startTime = parseTimestamp(timestamp[0])
            val endTime = parseTimestamp(timestamp[1])

            // Get subtitle text
            val text = lines.subList(2, lines.size).joinToString(" ")

            // Create Subtitle object and add to the list
            val subtitle = Subtitle(startTime, endTime, text)
            subtitles.add(subtitle)
        }
    }

    return subtitles
}

fun parseTimestamp(timestamp: String): Long {
    val parts = timestamp.split(":")
    val hours = parts[0].toLong() * 60 * 60 * 1000
    val minutes = parts[1].toLong() * 60 * 1000
    val seconds = parts[2].split(",")[0].toLong() * 1000
    val milliseconds = parts[2].split(",")[1].toLong()
    return hours + minutes + seconds + milliseconds
}

data class Subtitle(val startTime: Long, val endTime: Long, val text: String)

