package com.rizwansayyed.zene.ui.videoplayer.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.videoplayer.webview.WebAppInterface
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.VideoCardsViewWithSong
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun VideoRelated(webApp: WebAppInterface) {
    val viewModel: ZeneViewModel = hiltViewModel()
    var playlists by remember { mutableStateOf(false) }

    ImageIcon(R.drawable.ic_add_playlist, 26) {
        playlists = true
    }


    if (playlists) Dialog({ playlists = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardColors(MainColor, MainColor, MainColor, MainColor)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 7.dp)
            ) {
                item {
                    Spacer(Modifier.height(30.dp))
                }

                item {
                    TextPoppins(stringResource(R.string.related_video), false, size = 16, limit = 1)
                }

                item {
                    Spacer(Modifier.height(30.dp))
                }

                when (val v = viewModel.relatedVideos) {
                    APIResponse.Empty -> {}
                    is APIResponse.Error -> {}
                    APIResponse.Loading -> item {
                        Row(
                            Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
                        ) {
                            LoadingView(Modifier.size(32.dp))
                        }
                    }

                    is APIResponse.Success -> items(v.data) {
                        VideoCardsViewWithSong(it, v.data)
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            webApp.songInfo?.id?.let { viewModel.relatedVideos(it) }
        }
    }
}