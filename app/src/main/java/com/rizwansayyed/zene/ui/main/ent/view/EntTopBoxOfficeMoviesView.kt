package com.rizwansayyed.zene.ui.main.ent.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.URLSUtils.getSearchOnGoogle
import com.rizwansayyed.zene.utils.share.MediaContentUtils

@Composable
fun EntBoxOfficeMoviesLoadingView() {
    ShimmerEffect(
        Modifier
            .padding(12.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun EntTopBoxOfficeMoviesView() {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(80.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.top_movies_on_box_office), 23)
        }
        Spacer(Modifier.height(12.dp))
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntBoxOfficeMoviesItemView(data: ZeneMusicData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                val name = getSearchOnGoogle("${data.name}")
                MediaContentUtils.openCustomBrowser(name)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            data.thumbnail, data.name,
            Modifier
                .size(130.dp, 180.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(Modifier.fillMaxWidth()) {
            TextViewBold(data.name.orEmpty(), 20)
            Spacer(modifier = Modifier.height(8.dp))

            TextViewNormal(stringResource(R.string.weekend_gross, data.extra.orEmpty()), 14)
            TextViewNormal(stringResource(R.string.total_gross, data.extraInfo.orEmpty()), 14)
            TextViewNormal(stringResource(R.string.weeks_released, data.secId.orEmpty()), 14)

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star, null, Modifier.size(20.dp), Color(0xFFFFC107)
                )

                Spacer(modifier = Modifier.width(4.dp))
                TextViewNormal(data.artists.orEmpty(), 16)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }

}