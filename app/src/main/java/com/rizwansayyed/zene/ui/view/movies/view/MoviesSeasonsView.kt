package com.rizwansayyed.zene.ui.view.movies.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.ui.view.TextViewBold

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoviesSeasonsView(data: MoviesTvShowResponse) {
    if (data.seasons?.isNotEmpty() == true) {
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
                Box(Modifier) {
                    GlideImage(
                        it.thumbnail, it.name,
                        Modifier.size(100.dp, 200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}