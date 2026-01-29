package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntLoadingTrendingMoviesView() {
    val name by ipDB.collectAsState(null)

    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(20.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.trending_movies_shows_in, name?.country ?: ""), 23)
        }
        Spacer(Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(6) {
                ShimmerEffect(
                    Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .size(300.dp, 450.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntTrendingMoviesView(data: EntertainmentDiscoverResponse, viewModel: NavigationViewModel?) {
    val name by ipDB.collectAsState(null)

    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(20.dp))
        Row(Modifier.padding(horizontal = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            TextViewBold(stringResource(R.string.trending_movies_shows_in, name?.country ?: ""), 23)

            Spacer(Modifier.weight(1f))

            if (viewModel != null) Box(Modifier.clickable {
                viewModel.setEntNavigation(EntSectionSelector.MOVIES)
            }) {
                ImageIcon(R.drawable.ic_arrow_right, 29, Color.White)
            }
        }
        Spacer(Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(data.movies ?: emptyList()) { i, v ->
                TrendingPosterCard(v, i)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrendingPosterCard(v: ZeneMusicData, position: Int) {
    Box(
        Modifier
            .clip(RoundedCornerShape(24.dp))
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(v) },
                onClick = { startMedia(v) })
    ) {
        GlideImage(
            model = v.thumbnail,
            contentDescription = v.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .width(300.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.45f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(MainColor, RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.TrendingUp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextViewNormal("#${position + 1}", size = 15)
            }
        }
    }
}
