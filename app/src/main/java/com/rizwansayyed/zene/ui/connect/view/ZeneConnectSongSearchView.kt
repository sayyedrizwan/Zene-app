package com.rizwansayyed.zene.ui.connect.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.InputTypes
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SearchScreenBar
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.viewmodel.HomeViewModel


@Composable
fun ZeneConnectSongSearchView(click: (ZeneMusicDataItems?) -> Unit) {
    Dialog({ click(null) }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        val homeViewModel: HomeViewModel = hiltViewModel()
        var searchQuery by remember { mutableStateOf("") }

        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            Modifier
                .background(Color.Black)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            Arrangement.Top, Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            SearchScreenBar(InputTypes.SEARCH, searchQuery, {
                searchQuery = it
            }) {
                if (it.isEmpty()) return@SearchScreenBar
                homeViewModel.search(searchQuery)
                searchQuery = it
                keyboardController?.hide()
            }

            Spacer(Modifier.height(10.dp))

            when (val v = homeViewModel.searchQuery) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> TextPoppins(
                    stringResource(R.string.error_while_getting_song_try_again), size = 19
                )

                APIResponse.Loading -> LoadingView(Modifier.size(32.dp))
                is APIResponse.Success -> {
                    Spacer(Modifier.height(10.dp))

                    if (v.data.artists.isNotEmpty()) {
                        TextPoppins(stringResource(R.string.artists), size = 19)
                        v.data.artists.forEach { s ->
                            ConnectSearchItems(s, click)
                        }
                        Spacer(Modifier.height(30.dp))
                    }

                    if (v.data.videos.isNotEmpty()) {
                        TextPoppins(stringResource(R.string.videos), size = 19)
                        v.data.videos.forEach { s ->
                            ConnectSearchItems(s, click)
                        }
                        Spacer(Modifier.height(30.dp))
                    }

                    if (v.data.songs.isNotEmpty()) {
                        TextPoppins(stringResource(R.string.songs), size = 19)
                        v.data.songs.forEach { s ->
                            ConnectSearchItems(s, click)
                        }
                    }
                }
            }

            Spacer(Modifier.height(50.dp))
        }
    }
}

@Composable
fun ConnectSearchItems(song: ZeneMusicDataItems, click: (ZeneMusicDataItems?) -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clickable {
                click(song)
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        AsyncImage(
            imgBuilder(song.thumbnail),
            song.name,
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 9.dp)
        ) {
            TextPoppins(v = song.name ?: "", false, size = 16, limit = 1)
            Spacer(Modifier.height(4.dp))
            TextPoppins(v = song.artists ?: "", false, size = 13, limit = 1)
        }

        ImageIcon(R.drawable.ic_add, 19)
    }
}