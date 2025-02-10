package com.rizwansayyed.zene.ui.main.ai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun AIView(homeViewModel: HomeViewModel) {
    val context = LocalContext.current.applicationContext

    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.aiMusicList) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                item { Spacer(Modifier.height(25.dp)) }
                if (v.data.trending?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_ai_musics), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.trending) {
                            ItemCardView(it)
                        }
                    }
                }


                if (v.data.new?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.new_ai_musics), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(v.data.new?.chunked(10) ?: emptyList()) {
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(it) {
                            ItemCardView(it)
                        }
                    }
                    Spacer(Modifier.height(40.dp))
                }

                if (v.data.list?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.explore_tunes), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(v.data.list?.chunked(10) ?: emptyList()) {
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(it) {
                            ItemCardView(it)
                        }
                    }
                    Spacer(Modifier.height(40.dp))
                }
            }
        }

        item { Spacer(Modifier.height(200.dp)) }
    }

    LaunchedEffect(Unit) {
        homeViewModel.trendingAIMusic {
            CoroutineScope(Dispatchers.IO).launch {
                delay(5.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }
    }
}