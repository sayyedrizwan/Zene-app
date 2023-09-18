package com.rizwansayyed.zene.presenter.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.data.SongDataResponse
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import com.rizwansayyed.zene.presenter.ui.home.offline.OfflineSongsView
import com.rizwansayyed.zene.presenter.ui.home.online.OnlineSongsView
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.OfflineSongsViewModel
import kotlinx.coroutines.delay
import java.io.File
import kotlin.time.Duration.Companion.seconds


@Composable
fun MainHomePageView() {
    val nav: HomeNavViewModel = hiltViewModel()
    val offlineViewModel: OfflineSongsViewModel = hiltViewModel()

    val columnModifier = Modifier
        .fillMaxSize()
        .background(if (isSystemInDarkTheme()) DarkBlack else Color.White)


    LazyVerticalGrid(columns = GridCells.Fixed(2), columnModifier) {
//        item(span = { GridItemSpan(2) }) {
//            if (nav.isOnline.value)
//                OnlineSongsView()
//            else
//                OfflineSongsView()
//        }

        when (val v = offlineViewModel.allOfflineSongs.value) {
            SongDataResponse.Empty -> {}
            is SongDataResponse.Error -> {}
            SongDataResponse.Loading -> {}
            is SongDataResponse.Success -> items(v.item) {
                Column {
                    AsyncImage(
                        model = it.art,
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                    )
                    Text(text = it.title)
                    Text(text = it.artist)
                    Text(text = it.art.toString())
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(2.seconds)
        offlineViewModel.offlineSongsList()
    }
}