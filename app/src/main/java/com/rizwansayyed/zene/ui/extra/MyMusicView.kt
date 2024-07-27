package com.rizwansayyed.zene.ui.extra

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.extra.mymusic.TopMusicHeaders
import com.rizwansayyed.zene.ui.extra.playlists.AddPlaylistDialog
import com.rizwansayyed.zene.ui.extra.playlists.AddPlaylistView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

enum class MyMusicType {
    PLAYLISTS, HISTORY
}

@Composable
fun MyMusicView(viewModel: ZeneViewModel, close: () -> Unit) {
    val isThreeGrid = isScreenBig()

    var type by remember { mutableStateOf(MyMusicType.PLAYLISTS) }
    var page by remember { mutableIntStateOf(0) }
    var addPlaylist by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                TopMusicHeaders()
            }
        }

        item(2, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Row {
                Spacer(Modifier.height(6.dp))
                Box(
                    Modifier
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .clickable { type = MyMusicType.PLAYLISTS }
                        .border(
                            1.dp,
                            if (type == MyMusicType.PLAYLISTS) Color.White else Color.Black,
                            RoundedCornerShape(100)
                        )
                        .padding(vertical = 9.dp, horizontal = 18.dp)
                ) {
                    TextPoppins(stringResource(R.string.playlists_albums), size = 15)
                }

                AnimatedVisibility(visible = type == MyMusicType.PLAYLISTS) {
                    Box(
                        Modifier
                            .padding(vertical = 2.dp, horizontal = 6.dp)
                            .clip(RoundedCornerShape(100))
                            .background(Color.Black)
                            .clickable { addPlaylist = true }
                            .border(1.dp, Color.White, RoundedCornerShape(100))
                            .padding(vertical = 9.dp, horizontal = 18.dp)
                    ) {
                        ImageIcon(R.drawable.ic_add, size = 20)
                    }
                }

                Spacer(Modifier.height(6.dp))

                Box(
                    Modifier
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .clickable { type = MyMusicType.HISTORY }
                        .border(
                            1.dp,
                            if (type == MyMusicType.HISTORY) Color.White else Color.Black,
                            RoundedCornerShape(100)
                        )
                        .padding(vertical = 9.dp, horizontal = 18.dp)
                ) {
                    TextPoppins(stringResource(R.string.songs_history), size = 15)
                }
            }
        }

        item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(40.dp))
        }

        items(
            viewModel.songHistory,
            span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
            SongDynamicCards(it.asMusicData(), listOf(it.asMusicData()))
        }

        item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(40.dp))
        }

        if (viewModel.doShowMoreLoading) item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                Box(
                    Modifier
                        .padding(vertical = 15.dp, horizontal = 6.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .clickable {
                            page += 1
                            if (type == MyMusicType.PLAYLISTS) viewModel.playlists(page)
                            else viewModel.songHistory(page)
                        }
                        .border(1.dp, Color.White, RoundedCornerShape(100))
                        .padding(vertical = 9.dp, horizontal = 18.dp)
                ) {
                    TextPoppins(stringResource(R.string.load_more), size = 15)
                }
            }
        }


        item(1000, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(140.dp))
        }
    }

    if (addPlaylist) AddPlaylistDialog {
        addPlaylist = false
    }


    LaunchedEffect(type) {
        page = 0
        if (type == MyMusicType.PLAYLISTS) viewModel.playlists(0)
        else viewModel.songHistory(0)
    }
}