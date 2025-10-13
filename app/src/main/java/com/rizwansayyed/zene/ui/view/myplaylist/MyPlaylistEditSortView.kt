package com.rizwansayyed.zene.ui.view.myplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.util.UUID

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyPlaylistEditSortView(id: String, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val viewModel: MyLibraryViewModel = hiltViewModel()
        val hapticFeedback = LocalHapticFeedback.current
        val lazyListState = rememberLazyListState()

        val reorderableLazyListState = rememberReorderableLazyListState(
            lazyListState = lazyListState,
            onMove = { from, to ->
                val fromIndex = from.index
                val toIndex = to.index
                viewModel.moveItem(fromIndex, toIndex)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    items = viewModel.myPlaylistSongsList, key = { it.secId ?: "" }
                ) { song ->
                    ReorderableItem(reorderableLazyListState, song.secId ?: "") { isDragging ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isDragging) Color.DarkGray else Color.Transparent)
                                .padding(horizontal = 10.dp)
                                .padding(vertical = 16.dp)
                                .draggableHandle(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlideImage(
                                song.thumbnail,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(13.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = song.name ?: "",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                if (!song.artists.isNullOrEmpty()) {
                                    Text(
                                        text = song.artists,
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            Text(
                                text = "⋮⋮",
                                color = Color.Gray,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.myPlaylistSongsData(id)
        }
    }
}
