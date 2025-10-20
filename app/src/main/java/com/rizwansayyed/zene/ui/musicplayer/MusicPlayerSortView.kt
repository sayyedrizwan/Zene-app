package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayerSortView(close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val lazyListState = rememberLazyListState()
        val coroutine = rememberCoroutineScope()
        var coroutineJob by remember { mutableStateOf<Job?>(null) }
        val hapticFeedback = LocalHapticFeedback.current
        val music by musicPlayerDB.collectAsState(null)
        val list = remember { mutableStateListOf<ZeneMusicData?>() }

        fun updateList() {
            coroutineJob?.cancel()
            coroutineJob = coroutine.launch {
                delay(1.seconds)
                val musicPlayer = musicPlayerDB.firstOrNull()
                musicPlayer?.lists = list
                musicPlayerDB = flowOf(musicPlayer)
            }
        }

        val reorderableLazyListState = rememberReorderableLazyListState(
            lazyListState = lazyListState, onMove = { from, to ->
                val fromIndex = from.index
                val toIndex = to.index
                if (fromIndex in list.indices && toIndex in list.indices) {
                    list.add(toIndex, list.removeAt(fromIndex))
                }
                updateList()
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
            })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 16.dp)
            ) {
                TextViewBold(stringResource(R.string.song_s_playback_queue), 21)
            }

            LazyColumn(Modifier.fillMaxSize(), lazyListState) {
                itemsIndexed(list, key = { index, v -> "${v?.id}_${v?.type}" }) { index, song ->
                    ReorderableItem(
                        reorderableLazyListState, key = "${song?.id}_${song?.type}"
                    ) { isDragging ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isDragging) MaterialTheme.colorScheme.surface else Color.Transparent)
                                .padding(vertical = 15.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlideImage(
                                song?.thumbnail ?: "",
                                song?.name ?: "",
                                Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                TextViewBold(song?.name ?: "", 14, line = 2)
                                TextViewNormal(song?.artists ?: "", 13, line = 2)
                            }

                            if (music?.data == song) {
                                GlideImage(
                                    R.raw.wave_animiation,
                                    "wave",
                                    Modifier.size(40.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Row(Modifier.clickable {
                                    list.removeAt(index)
                                    updateList()
                                }) {
                                    ImageIcon(R.drawable.ic_cancel_close, 18)
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.DragHandle,
                                contentDescription = "Reorder",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .draggableHandle()
                                    .padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            val musicList = DataStorageManager.musicPlayerDB.firstOrNull()
            list.addAll(musicList?.lists?.toList() ?: emptyList())
        }
    }
}