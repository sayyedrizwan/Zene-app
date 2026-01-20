package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.ent.view.EntStreamingToday
import com.rizwansayyed.zene.ui.main.ent.view.EntStreamingTodayItems
import com.rizwansayyed.zene.ui.main.ent.view.EntStreamingTodayLoading

@Composable
fun EntertainmentMoviesView(viewModel: EntertainmentViewModel) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 250.dp)
    ) {
//        when (val v = viewModel.discover) {
//            ResponseResult.Empty -> {}
//            is ResponseResult.Error -> {}
//            ResponseResult.Loading -> item(span = { GridItemSpan(2) }) {
//                EntLoadingTrendingMoviesView()
//            }
//            is ResponseResult.Success -> {
//                if (v.data.movies?.isNotEmpty() == true)
//                    item(key = "movies", span = { GridItemSpan(2) }) {
//                        EntTrendingMoviesView(v.data)
//                    }
//            }
//        }
//
//        when (val v = viewModel.trailers) {
//            ResponseResult.Empty -> {}
//            is ResponseResult.Error -> {}
//            ResponseResult.Loading -> {
//                item(key = "trailer_header", span = { GridItemSpan(2) }) {
//                    EntTrailerView()
//                }
//
//                items(6) {
//                    ShimmerEffect(
//                        Modifier
//                            .padding( 5.dp)
//                            .aspectRatio(1f)
//                            .clip(RoundedCornerShape(16.dp))
//                    )
//                }
//            }
//
//            is ResponseResult.Success -> {
//                if (v.data.isNotEmpty()) {
//                    item(key = "trailer_header", span = { GridItemSpan(2) }) {
//                        EntTrailerView()
//                    }
//
//                    items(v.data, span = { GridItemSpan(2) }) {
//                        EntTrailerItemsView(it)
//                    }
//                }
//            }
//        }

        when (val v = viewModel.streaming) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item(key = "streaming_header", span = { GridItemSpan(2) }) {
                    EntStreamingToday()
                }

                items(5, span = { GridItemSpan(2) }) {
                    EntStreamingTodayLoading()
                }
            }

            is ResponseResult.Success -> {
                if (v.data.isNotEmpty()) {
                    item(key = "streaming_header", span = { GridItemSpan(2) }) {
                        EntStreamingToday()
                    }

                    items(v.data, span = { GridItemSpan(2) }) {
                        EntStreamingTodayItems(it)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.entTrailers()
        viewModel.entStreamingTrending()
    }
}