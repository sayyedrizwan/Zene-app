package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
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
import com.bumptech.glide.integration.compose.placeholder
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.service.notification.NavigationUtils.triggerInfoSheet
import com.rizwansayyed.zene.viewmodel.EntertainmentViewModel
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.formatNews
import com.rizwansayyed.zene.utils.share.MediaContentUtils.openCustomBrowser

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntDiscoverTopView(data: ZeneMusicData, viewModel: EntertainmentViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        GlideImage(
            data.thumbnail, data.name,
            Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            DarkCharcoal.copy(alpha = 0.75f),
                            DarkCharcoal.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            BreakingStoryTag()
            Spacer(Modifier.height(12.dp))
            TextViewBold(data.name ?: "", size = 20, line = 3)
            Spacer(Modifier.height(12.dp))
            ReadingNow(viewModel, data)
        }
    }
}


@Composable
fun BreakingStoryTag() {
    Row(
        modifier = Modifier
            .background(
                color = Color(0xFFB63A5B), shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Whatshot,
            contentDescription = null,
            tint = Color.White, modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(Modifier.offset(y = (1).dp)) {
            TextViewSemiBold(stringResource(R.string.breaking_story).uppercase(), size = 13)
        }
    }
}

@Composable
fun ReadingNow(viewModel: EntertainmentViewModel, data: ZeneMusicData) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(MainColor)
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_user_multiple, 20)
            Spacer(modifier = Modifier.width(6.dp))
            TextViewNormal(
                "+${viewModel.trendingNewsNumber.formatNews()} ${stringResource(R.string.reading_now)}",
                size = 16
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 10.dp)
                .clip(RoundedCornerShape(100))
                .clickable {
                    openCustomBrowser(data.id)
                }
                .background(MainColor)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_arrow_right, 18, Color.White)
        }

        Row(
            modifier = Modifier
                .padding(start = 10.dp)
                .clip(RoundedCornerShape(100))
                .clickable {
                    triggerInfoSheet(data)
                }
                .background(MainColor)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_vertical_menu, 18, Color.White)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NetworkImage(
    url: String, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.Crop
) {
    GlideImage(
        model = url,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        loading = placeholder(R.drawable.circle_image),
        failure = placeholder(R.drawable.circle_image)
    )
}