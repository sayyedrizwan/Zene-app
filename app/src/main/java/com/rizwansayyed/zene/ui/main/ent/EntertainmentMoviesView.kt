package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLoadingTrendingMoviesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingMoviesView

@Composable
fun EntertainmentMoviesView(viewModel: EntertainmentViewModel) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 250.dp)) {
        when (val v = viewModel.discover) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                EntLoadingTrendingMoviesView()
            }
            is ResponseResult.Success -> {
                if (v.data.movies?.isNotEmpty() == true)
                    item(key = "movies") { EntTrendingMoviesView(v.data) }
            }
        }
    }
}