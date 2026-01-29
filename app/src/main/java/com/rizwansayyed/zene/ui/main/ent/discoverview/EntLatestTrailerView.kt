package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
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
import com.rizwansayyed.zene.data.model.EntertainmentDiscoverResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@Composable
fun EntLatestTrailerView(data: EntertainmentDiscoverResponse, viewModel: NavigationViewModel) {
    if (data.featuredTrailer?.id != null || data.trailers?.isNotEmpty() == true) {
        Spacer(Modifier.height(50.dp))

        Row(Modifier.padding(horizontal = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            TextViewBold(stringResource(R.string.latest_trailers), 23)

            Spacer(Modifier.weight(1f))

            Box(Modifier.clickable {
                viewModel.setEntNavigation(EntSectionSelector.MOVIES)
            }) {
                ImageIcon(R.drawable.ic_arrow_right, 29, Color.White)
            }
        }

        Spacer(Modifier.height(6.dp))
    }
    if (data.featuredTrailer?.id != null) FeaturedTrailerCard(data.featuredTrailer)
    if (data.trailers?.isNotEmpty() == true) TrailerRow(data.trailers)
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeaturedTrailerCard(trailer: ZeneMusicData) {
    Box(modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth()
        .height(450.dp)
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

        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            TextViewSemiBold(stringResource(R.string.trending_), size = 15)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            TextViewBold(trailer.name ?: "", 27, line = 2)

            Spacer(modifier = Modifier.height(15.dp))

            PlayTrailerButton {
                MediaContentUtils.startMedia(trailer)
            }
        }
    }
}

@Composable
fun PlayTrailerButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_play, 27, Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            TextViewNormal(stringResource(R.string.play_trailer), 19, Color.Black)
        }
    }
}


@Composable
fun TrailerRow(trailers: List<ZeneMusicData>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(trailers) {
            TrailerThumbnail(it)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrailerThumbnail(data: ZeneMusicData) {
    Column(Modifier.width(120.dp)) {
        Box(
            Modifier
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    MediaContentUtils.startMedia(data)
                }
        ) {
            GlideImage(
                model = data.thumbnail,
                contentDescription = data.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        TextViewNormal(data.name ?: "", 15, line = 2)
    }
}
