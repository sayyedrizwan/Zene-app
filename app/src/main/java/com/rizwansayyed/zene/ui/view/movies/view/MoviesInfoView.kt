package com.rizwansayyed.zene.ui.view.movies.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun MoviesInfoView(data: MoviesTvShowResponse) {
    Row(
        Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        Arrangement.Start, Alignment.CenterVertically
    ) {
        if (data.score?.imdbScore != null) {
            Spacer(Modifier.width(20.dp))
            ImageIcon(R.drawable.ic_imdb, 20, null)
            Spacer(Modifier.width(5.dp))
            TextViewNormal(data.score.imdbScore.toString(), 18)
        }

        if (data.score?.tmdbScore != null) {
            Spacer(Modifier.width(20.dp))
            ImageIcon(R.drawable.ic_the_movie_db, 20, null)
            Spacer(Modifier.width(5.dp))
            TextViewNormal(data.score.tmdbScore.toString(), 18)
        }

        if (data.score?.tomatoMeter != null) {
            Spacer(Modifier.width(20.dp))
            Image(
                painterResource(R.drawable.ic_rotten_tomatoes_logo), "",
                Modifier.height(30.dp).width(45.dp)
            )
            Spacer(Modifier.width(5.dp))
            TextViewNormal("${data.score.tomatoMeter}%", 18)
        }

        Spacer(Modifier.width(10.dp))

    }
}