package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.EntertainmentDiscoverResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.URLSUtils.getSearchNewsOnGoogle
import com.rizwansayyed.zene.utils.share.MediaContentUtils

val DarkBg = Color(0xFF120A0A)
val CardBg = Color(0xFF1C1212)
val AccentRed = Color(0xFFFF5A3C)

val MoviesBlue = Color(0xFF4DA3FF)
val TvPurple = Color(0xFFB48CFF)
val AwardsGold = Color(0xFFFFC44D)
val StreamingGreen = Color(0xFF4DFF9A)
val GossipPink = Color(0xFFFF6EC7)
val DisneyTeal = Color(0xFF3ED6C6)

@Composable
fun EntTrendingTopicsView(data: EntertainmentDiscoverResponse) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(Modifier.height(30.dp))
            Box(Modifier.padding(horizontal = 6.dp)) {
                TextViewBold(stringResource(R.string.trending_topics), 23)
            }
            Spacer(Modifier.height(12.dp))

            data.trends?.forEachIndexed { i, v ->
                if (i == 0) TrendingMainCard(v)
            }
            Spacer(Modifier.height(20.dp))

            val items = data.trends?.drop(1).orEmpty()
            items.chunked(2).forEachIndexed { rowIndex, rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEachIndexed { colIndex, item ->
                        val globalIndex = rowIndex * 2 + colIndex
                        item.name?.let {
                            TrendingSmallCard(
                                it, globalIndex,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }

                    if (rowItems.size == 1) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

        }
    }
}


@Composable
fun TrendingMainCard(v: ZeneMusicData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                v.name?.let {
                    MediaContentUtils.openCustomBrowser(getSearchNewsOnGoogle(it))
                }
            }
            .clip(RoundedCornerShape(24.dp))
            .background(DarkCharcoal)
            .border(
                width = 1.5.dp, brush = Brush.horizontalGradient(
                    listOf(MainColor, Color.Transparent)
                ), shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        TextViewNormal(stringResource(R.string.one_trending), size = 12)
        Spacer(Modifier.height(8.dp))
        v.name?.let { TextViewBold(it, size = 21) }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun TrendingSmallCard(
    title: String, i: Int, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable {
                MediaContentUtils.openCustomBrowser(getSearchNewsOnGoogle(title))
            }
            .clip(RoundedCornerShape(22.dp))
            .background(DarkCharcoal.copy(alpha = 0.6f))
            .border(
                1.dp, MainColor, RoundedCornerShape(22.dp)
            )
            .padding(16.dp)
    ) {
        TextViewNormal("#${i + 2} ${stringResource(R.string.trending_)}", size = 12)
        Spacer(Modifier.height(8.dp))
        TextViewBold(title, size = 15)
    }
}
