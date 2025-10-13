package com.rizwansayyed.zene.ui.videoplayer.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.VideoCardView
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import com.rizwansayyed.zene.viewmodel.PlayingVideoInfoViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerSimilarView(viewModel: PlayingVideoInfoViewModel) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    var showAlert by remember { mutableStateOf(false) }

    ImageWithBorder(R.drawable.ic_discover_circle) {
        showAlert = true
    }

    if (showAlert) ModalBottomSheet(
        { showAlert = false }, contentColor = MainColor, containerColor = MainColor
    ) {
        val coroutines = rememberCoroutineScope()
        var job by remember { mutableStateOf<Job?>(null) }

        LazyVerticalGrid(
            GridCells.Fixed(2),
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(20.dp))
                Box(Modifier.padding(horizontal = 6.dp)) {
                    TextViewNormal(stringResource(R.string.similar_videos), 19)
                }
                Spacer(Modifier.height(4.dp))
            }

            when (val v = playerViewModel.videoSimilarVideos) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> item(span = { GridItemSpan(maxLineSpan) }) { CircularLoadingView() }
                is ResponseResult.Success -> items(v.data.second) {
                    VideoCardView(it)
                }
            }

        }

        LaunchedEffect(Unit) {
            playerViewModel.similarVideos(viewModel.videoID)
        }

        DisposableEffect(Unit) {
            job?.cancel()
            job = coroutines.safeLaunch {
                while (true) {
                    viewModel.showControlView(true)
                    delay(1.seconds)
                }
            }
            onDispose { job?.cancel() }
        }
    }
}