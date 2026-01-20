package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLoadingTrendingMoviesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingMoviesView
import com.rizwansayyed.zene.ui.main.ent.view.EntBoxOfficeMoviesItemView
import com.rizwansayyed.zene.ui.main.ent.view.EntBoxOfficeMoviesLoadingView
import com.rizwansayyed.zene.ui.main.ent.view.EntStreamingToday
import com.rizwansayyed.zene.ui.main.ent.view.EntStreamingTodayItems
import com.rizwansayyed.zene.ui.main.ent.view.EntStreamingTodayLoading
import com.rizwansayyed.zene.ui.main.ent.view.EntTopBoxOfficeMoviesView
import com.rizwansayyed.zene.ui.main.ent.view.EntTrailerItemsView
import com.rizwansayyed.zene.ui.main.ent.view.EntTrailerView
import com.rizwansayyed.zene.ui.main.ent.view.EntUpcomingMoviesItemView
import com.rizwansayyed.zene.ui.main.ent.view.EntUpcomingMoviesView
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun EntertainmentMoviesView(viewModel: EntertainmentViewModel) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 250.dp)
    ) {
        when (val v = viewModel.discover) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item(span = { GridItemSpan(2) }) {
                EntLoadingTrendingMoviesView()
            }

            is ResponseResult.Success -> {
                if (v.data.movies?.isNotEmpty() == true) item(
                    key = "movies",
                    span = { GridItemSpan(2) }) {
                    EntTrendingMoviesView(v.data)
                }
            }
        }

        when (val v = viewModel.trailers) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item(key = "trailer_header", span = { GridItemSpan(2) }) {
                    EntTrailerView()
                }

                items(6) {
                    ShimmerEffect(
                        Modifier
                            .padding(5.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            is ResponseResult.Success -> {
                if (v.data.isNotEmpty()) {
                    item(key = "trailer_header", span = { GridItemSpan(2) }) {
                        EntTrailerView()
                    }

                    items(v.data, span = { GridItemSpan(2) }) {
                        EntTrailerItemsView(it)
                    }
                }
            }
        }

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


        when (val v = viewModel.boxOfficeMovie) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item(key = "box_office_header", span = { GridItemSpan(2) }) {
                    EntTopBoxOfficeMoviesView()
                }

                items(5, span = { GridItemSpan(2) }) {
                    EntBoxOfficeMoviesLoadingView()
                }
            }

            is ResponseResult.Success -> {
                if (v.data.isNotEmpty()) {
                    item(key = "box_office_header", span = { GridItemSpan(2) }) {
                        EntTopBoxOfficeMoviesView()
                    }

                    items(v.data, span = { GridItemSpan(2) }) {
                        EntBoxOfficeMoviesItemView(it)
                    }
                }
            }
        }

        when (val v = viewModel.upcomingMovies) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item(key = "upcoming_movies_header", span = { GridItemSpan(2) }) {
                    EntUpcomingMoviesView()
                }

                items(5, span = { GridItemSpan(2) }) {
                    EntBoxOfficeMoviesLoadingView()
                }
            }

            is ResponseResult.Success -> {
                if (v.data.isNotEmpty()) {
                    item(key = "upcoming_movies_header", span = { GridItemSpan(2) }) {
                        EntUpcomingMoviesView()
                    }

                    v.data.forEachIndexed { i, item ->
                        if (i != 0) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .height(1.dp),
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        item {
                            Row(
                                Modifier
                                    .padding(horizontal = 6.dp)
                                    .padding(top = 20.dp)
                            ) {
                                Spacer(Modifier.width(9.dp))
                                TextViewSemiBold(item.date.orEmpty(), 22)
                            }
                        }

                        items(item.list, span = { GridItemSpan(2) }) {
                            EntUpcomingMoviesItemView(it)
                        }
                    }

                }
            }
        }


    }

    LaunchedEffect(Unit) {
        viewModel.entTrailers()
        viewModel.entStreamingTrending()
        viewModel.entBoxOfficeMovie()
        viewModel.entUpcomingMovie()
    }
}