package com.rizwansayyed.zene.ui.view.movies.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoviesSeasonsView(data: MoviesTvShowResponse) {
    if (data.seasons?.isNotEmpty() == true) {
        var showSeasonInfo by remember { mutableStateOf<ZeneMusicData?>(null) }

        Spacer(Modifier.height(50.dp))
        Box(
            Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth()
        ) {
            TextViewBold(stringResource(R.string.seasons), 23)
        }

        Spacer(Modifier.height(12.dp))
        LazyRow(Modifier.fillMaxWidth()) {
            items(data.seasons) {
                Column(Modifier
                    .padding(horizontal = 6.dp)
                    .clickable { showSeasonInfo = it }) {
                    GlideImage(it.thumbnail, it.name, Modifier.width(250.dp))
                    Spacer(Modifier.height(5.dp))
                    Column(Modifier.padding(horizontal = 5.dp)) {
                        TextViewSemiBold("${it.name} • ${it.artists}", 14, line = 2)
                    }
                }
            }
        }

        if (showSeasonInfo != null) SeasonsInfoView(showSeasonInfo!!, data) {
            showSeasonInfo = null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun SeasonsInfoView(seasons: ZeneMusicData, data: MoviesTvShowResponse, close: () -> Unit) {
    ModalBottomSheet(close, contentColor = MainColor, containerColor = MainColor) {
        val viewModel: HomeViewModel = hiltViewModel()
        LazyColumn(Modifier.fillMaxWidth()) {
            item { Spacer(Modifier.height(10.dp)) }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    Arrangement.Center, Alignment.CenterVertically
                ) {
                    GlideImage(seasons.thumbnail, seasons.name, Modifier.height(140.dp))
                    Column(
                        Modifier
                            .padding(horizontal = 13.dp)
                            .weight(1f)
                    ) {
                        TextViewBold(data.name ?: "", 30, line = 1)
                        TextViewNormal(seasons.name ?: "", 16, line = 1)
                    }
                }
            }

            when (val v = viewModel.seasonsMovieShowInfo) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> item { CircularLoadingView() }
                is ResponseResult.Success -> {
                    items(v.data) { EpisodesItemsView(it) }
                }
            }

            item { Spacer(Modifier.height(250.dp)) }
        }

        LaunchedEffect(Unit) {
            seasons.id?.let { viewModel.moviesTvShowsSeasonsInfo(it) }
        }
    }
}

@Composable
fun EpisodesItemsView(data: ZeneMusicData) {
    var showFullDesc by remember { mutableStateOf(false) }
    Row(
        Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black)
            .clickable { showFullDesc = !showFullDesc }
            .padding(horizontal = 5.dp, vertical = 20.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Column(
            Modifier.padding(horizontal = 10.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextViewBold(data.extra ?: "", 60)
            TextViewNormal(stringResource(R.string.episode), 14)
        }

        Column(
            Modifier
                .padding(horizontal = 5.dp)
                .weight(1f)
        ) {
            TextViewBold(data.name ?: "", 19)
            TextViewNormal(data.artists ?: "", 14, line = if (showFullDesc) 200 else 2)
        }
    }
}