package com.rizwansayyed.zene.ui.main.ent

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.ent.view.TopSliderVideoNewsView
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ItemYoutubeCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun EntertainmentNewsView() {
    val homeViewModel: HomeViewModel = hiltViewModel()

    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.entertainmentData) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                item {
                    Spacer(Modifier.height(70.dp))
                    Box(Modifier.padding(horizontal = 9.dp)) {
                        TextViewBold(stringResource(R.string.entertainment_), 35)
                    }
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    TopSliderVideoNewsView(v.data.topNews)
                }


                item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.latest_news_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.latestNews ?: emptyList()) {
                            ItemYoutubeCardView(it)
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(150.dp))
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.entertainmentNewsData()
    }
}