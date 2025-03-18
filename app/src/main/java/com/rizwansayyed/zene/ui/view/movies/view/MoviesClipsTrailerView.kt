package com.rizwansayyed.zene.ui.view.movies.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.ui.view.MoviesImageCard
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.VideoCardView

@Composable
fun MoviesClipsTrailerView(data: MoviesTvShowResponse) {
    if (data.clips?.isNotEmpty() == true) {
        Spacer(Modifier.height(50.dp))
        Box(Modifier.padding(horizontal = 6.dp).fillMaxWidth()) {
            TextViewBold(stringResource(R.string.trailers_teasers_featurettes), 23)
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(Modifier.fillMaxWidth()) {
            items(data.clips) { VideoCardView(it) }
        }
    }
}

@Composable
fun SimilarMoviesClipsView(data: MoviesTvShowResponse) {
    if (data.similar?.isNotEmpty() == true) {
        Spacer(Modifier.height(50.dp))
        Box(Modifier.padding(horizontal = 6.dp).fillMaxWidth()) {
            TextViewBold(stringResource(R.string.similar_movies_shows), 23)
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(Modifier.fillMaxWidth()) {
            items(data.similar) {  MoviesImageCard(it) }
        }
    }
}