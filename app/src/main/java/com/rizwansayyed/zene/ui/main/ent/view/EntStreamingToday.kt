package com.rizwansayyed.zene.ui.main.ent.view

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.StreamingTrendingResponse
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia

@Composable
fun EntStreamingToday() {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(80.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.today_s_top_streaming), 23)
        }
        Spacer(Modifier.height(12.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntStreamingTodayItems(data: StreamingTrendingResponse) {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(10.dp))
        Row(Modifier.padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            GlideImage(
                data.icon,
                data.name,
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .size(35.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(9.dp))
            TextViewSemiBold(data.name.orEmpty(), 19)
        }
        Spacer(Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 13.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(data.items) { i, v ->
                Box(
                    Modifier.combinedClickable(
                        onLongClick = { NavigationUtils.triggerInfoSheet(v) },
                        onClick = { startMedia(v) })
                ) {
                    GlideImage(
                        v.thumbnail, v.name,
                        Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .height(250.dp),
                        contentScale = ContentScale.Fit
                    )

                    Box(Modifier
                        .align(Alignment.BottomStart)
                        .offset(y = (9).dp)) {
                        TextViewBold("${i + 1}", 45)
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}

@Composable
fun EntStreamingTodayLoading() {
    Column(Modifier.fillMaxWidth()) {
        Box(Modifier.padding(horizontal = 10.dp)) {
            ShimmerEffect(
                Modifier
                    .padding(horizontal = 3.dp)
                    .size(160.dp, 10.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 13.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(6) {
                ShimmerEffect(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .size(200.dp, 250.dp)
                )
            }
        }

        Spacer(Modifier.height(25.dp))
    }
}