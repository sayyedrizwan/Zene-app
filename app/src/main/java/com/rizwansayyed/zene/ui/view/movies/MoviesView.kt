package com.rizwansayyed.zene.ui.view.movies

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.movies.view.MoviesCastInfoView
import com.rizwansayyed.zene.ui.view.movies.view.MoviesCategories
import com.rizwansayyed.zene.ui.view.movies.view.MoviesClipsTrailerView
import com.rizwansayyed.zene.ui.view.movies.view.MoviesInfoView
import com.rizwansayyed.zene.ui.view.movies.view.MoviesSeasonsView
import com.rizwansayyed.zene.ui.view.movies.view.MoviesTopView
import com.rizwansayyed.zene.ui.view.movies.view.SimilarMoviesClipsView
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun MoviesView(id: String) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalActivity.current

    Box(Modifier.fillMaxSize(), Alignment.Center) {
        when (val v = viewModel.movieShowInfo) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> CircularLoadingView()
            is ResponseResult.Success -> {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item { MoviesTopView(v.data) }
                    item { MoviesCategories(v.data) }
                    item { Spacer(Modifier.height(10.dp)) }
                    item { MoviesInfoView(v.data) }
                    item { Spacer(Modifier.height(10.dp)) }
                    item { MoviesSeasonsView(v.data) }
                    item { Spacer(Modifier.height(10.dp)) }
                    item { MoviesClipsTrailerView(v.data) }
                    item { MoviesCastInfoView(v.data) }
                    item { SimilarMoviesClipsView(v.data) }

                    item { Spacer(Modifier.height(350.dp)) }
                }
            }
        }

        ButtonArrowBack(Modifier.align(Alignment.TopStart))
    }

    LaunchedEffect(Unit) {
        if (viewModel.movieShowInfo !is ResponseResult.Success) {
            val idName = id.replace("^", "/")
            viewModel.moviesTvShowsInfo(idName)
        }

        context?.let { InterstitialAdsUtils(it) }
    }
}

