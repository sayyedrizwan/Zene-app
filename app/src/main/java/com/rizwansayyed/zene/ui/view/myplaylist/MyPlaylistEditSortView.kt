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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyPlaylistEditSortView(id: String, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val viewModel: MyLibraryViewModel = hiltViewModel()
        val hapticFeedback = LocalHapticFeedback.current
        val lazyListState = rememberLazyListState()
        var isBottomTriggered by remember { mutableStateOf(false) }

        val reorderableLazyListState = rememberReorderableLazyListState(
            lazyListState = lazyListState,
            onMove = { from, to ->
                val fromIndex = from.index
                val toIndex = to.index
                viewModel.moveItem(fromIndex, toIndex, id)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
            }
        )

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
                TextViewBold(stringResource(R.string.reorder_playlist_songs), 21)
            }

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
                                .padding(vertical = 16.dp),
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
                                TextViewBold(song.name ?: "", 15)
                                if (!song.artists.isNullOrEmpty()) {
                                    TextViewNormal(song.artists, 15, Color.Gray)
                                }
                            }

                            Box(Modifier.draggableHandle()) {
                                TextViewBold("⋮⋮", 20)
                            }
                        }
                    }
                }
            }

            if (viewModel.myPlaylistSongsIsLoading) Row(Modifier) {
                CircularLoadingView()
            }
        }

        LaunchedEffect(Unit) {
            viewModel.myPlaylistSongsData(id, SortMyPlaylistType.CUSTOM_ORDER)
        }


        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.layoutInfo }.collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                if (lastVisibleItemIndex >= totalItemsCount - 1 && !isBottomTriggered) {
                    isBottomTriggered = true
                    viewModel.myPlaylistSongsData(id, SortMyPlaylistType.CUSTOM_ORDER)
                } else if (lastVisibleItemIndex < totalItemsCount - 1) {
                    isBottomTriggered = false
                }
            }
        }
    }
}
