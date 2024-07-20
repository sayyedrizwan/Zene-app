package com.rizwansayyed.zene.ui.player.view

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.ZeneLyricsData
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LyricsView(lyricsData: ZeneLyricsData, playerInfo: MusicPlayerData?) {
    var job by remember { mutableStateOf<Job?>(null) }
    val coroutine = rememberCoroutineScope()
    var lyrics by remember { mutableStateOf("") }
    var isSync by remember { mutableStateOf(false) }
    var lyricsDone by remember { mutableIntStateOf(0) }
    var userIsScrolling by remember { mutableStateOf(false) }

    val pager = rememberPagerState(pageCount = { lyrics.split("<br>").size })

    if (lyrics != "") if (isSync)
        VerticalPager(
            pager,
            Modifier
                .padding(top = 40.dp, bottom = 10.dp)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(DarkCharcoal),
            PaddingValues(vertical = 135.dp),
        ) { page ->
            if (page == 0) {
                Spacer(Modifier.height(5.dp))
                Row(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                    TextPoppins(stringResource(R.string.lyrics), size = 25)
                }
                Spacer(Modifier.height(5.dp))
            } else if (page == 1) {
                Spacer(Modifier.height(20.dp))
            }

            TextPoppins(
                lyrics.split("<br>")[page].substringAfter("]").trim(),
                true, if (lyricsDone > page) Color.White else Color.DarkGray,
                if (lyricsDone > page) 19 else 16
            )
        }
    else Column(
        Modifier
            .padding(top = 40.dp, bottom = 10.dp)
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(DarkCharcoal)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(5.dp))
        Row(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            TextPoppins(stringResource(R.string.lyrics), size = 25)
        }
        Spacer(Modifier.height(5.dp))

        TextPoppins(lyrics, true, size = 16)

        Spacer(Modifier.height(5.dp))
    }


    LaunchedEffect(playerInfo?.currentDuration) {
        if (lyricsData.isSync == true) {
            isSync = true
            lyrics.split("<br>").forEachIndexed { index, s ->
                if (s.contains(playerInfo?.formatCurrentDuration() ?: "")) {
                    lyricsDone = index + 1
                    if (!userIsScrolling) pager.animateScrollToPage(index + 1)
                }
            }
            return@LaunchedEffect
        }
        if ((lyricsData.lyrics?.trim()?.length ?: 0) > 5) lyrics = lyricsData.lyrics ?: ""
    }

    LaunchedEffect(Unit) {
        pager.interactionSource.interactions
            .collect {
                if (it is DragInteraction.Start) {
                    userIsScrolling = true
                    job?.cancel()
                    job = coroutine.launch {
                        delay(2.seconds)
                        userIsScrolling = false
                    }
                }
            }
    }

}