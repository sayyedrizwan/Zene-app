package com.rizwansayyed.zene.ui.main.ent.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.ent.discoverview.FeaturedTrailerCard
import com.rizwansayyed.zene.ui.main.ent.discoverview.PlayTrailerButton
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils

@Composable
fun EntTrailerView() {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(50.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.latest_trailers), 23)
        }
        Spacer(Modifier.height(12.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntTrailerItemsView(trailer: ZeneMusicData) {
    Box(modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth()
        .height(250.dp)
        .clickable {
            MediaContentUtils.startMedia(trailer)
        }
        .clip(RoundedCornerShape(28.dp))) {

        GlideImage(
            model = trailer.thumbnail,
            contentDescription = trailer.thumbnail,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.15f), Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            TextViewBold(trailer.name ?: "", 15, line = 2)
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}