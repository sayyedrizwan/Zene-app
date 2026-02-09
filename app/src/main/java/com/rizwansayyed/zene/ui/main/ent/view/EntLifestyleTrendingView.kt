package com.rizwansayyed.zene.ui.main.ent.view

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia

@Composable
fun EntLifestyleTrendingLoadingView() {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(5.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.trending_looks), 23)
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
                        .size(260.dp, 360.dp)
                )
            }
        }
    }
}

@Composable
fun EntLifestyleTrendingView(data: ZeneMusicDataList) {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(5.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.trending_looks), 23)
        }
        Spacer(Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data) {
                LifestyleLookCard(it)
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LifestyleLookCard(item: ZeneMusicData) {
    Column(
        Modifier
            .width(260.dp)
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(item) },
                onClick = { startMedia(item) }
            )) {
        Box {
            GlideImage(
                item.thumbnail,
                item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                removeLifestyleDate(item.artists.orEmpty()),
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .background(
                        Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = proximanOverFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        TextViewBold(item.name.orEmpty())
        TextViewNormal(extractDate(item.artists.orEmpty()), 14)
    }
}


@Composable
fun EntLifestyleLatestView() {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(5.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.latest_looks), 23)
        }
        Spacer(Modifier.height(12.dp))
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntLifestyleLatestItemView(data: ZeneMusicData) {
    Card(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(180.dp),
        RoundedCornerShape(24.dp),
        CardDefaults.cardColors(Color.Black),
        CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            Modifier
                .combinedClickable(
                    onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                    onClick = { startMedia(data) }
                )
                .fillMaxSize()
                .padding(16.dp), Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f), Arrangement.Center) {
                Column(Modifier.fillMaxHeight(), Arrangement.Center) {
                    Spacer(modifier = Modifier.height(6.dp))
                    TextViewBold(data.name.orEmpty(), 20)
                    Spacer(modifier = Modifier.height(5.dp))
                    TextViewNormal(data.artists.orEmpty(), 15)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            GlideImage(
                data.thumbnail,
                data.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            )
        }
    }
}


fun removeLifestyleDate(text: String): String {
    return text.replace(
        Regex("\\s+[A-Z][a-z]+\\s+\\d{1,2},\\s+\\d{4}$"), ""
    )
}
fun extractDate(text: String): String {
    val regex = Regex("[A-Z][a-z]+\\s+\\d{1,2},\\s+\\d{4}")
    return regex.find(text)?.value ?: text
}
