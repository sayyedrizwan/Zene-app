package com.rizwansayyed.zene.ui.main.podcast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PodcastView(podcast: String) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext

    val width = (LocalConfiguration.current.screenWidthDp / 1.5).dp
    var fullDesc by remember { mutableStateOf(false) }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(70.dp)) }
        when (val v = homeViewModel.podcastData) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item { CircularLoadingView() }
            is ResponseResult.Success -> item {
                GlideImage(
                    v.data.thumbnail, v.data.name,
                    modifier = Modifier
                        .size(width)
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(15.dp))
                TextViewBoldBig(v.data.name ?: "", 40, center = true)
                Spacer(Modifier.height(15.dp))
                TextViewSemiBold(stringResource(R.string.podcast), 17, center = true)
                Spacer(Modifier.height(15.dp))
                TextViewNormal(
                    v.data.artists ?: "", 14, center = true, line = if (fullDesc) 1000 else 3
                )
                Spacer(Modifier.height(5.dp))

                Box(
                    Modifier
                        .rotate(if (fullDesc) 180f else 0f)
                        .clickable { fullDesc = !fullDesc }) {
                    ImageIcon(R.drawable.ic_arrow_down, 28)
                }

                Spacer(Modifier.height(70.dp))
            }
        }

        item { Spacer(Modifier.height(300.dp)) }
    }

    LaunchedEffect(Unit) {
        homeViewModel.podcastData(podcast) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(5.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }
        homeViewModel.podcastDataList(podcast)
    }
}