package com.rizwansayyed.zene.ui.main.ent.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils

@Composable
fun EntTrailerView() {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(80.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.latest_trailers), 23)
        }
        Spacer(Modifier.height(12.dp))
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntTrailerItemsView(trailer: ZeneMusicData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 9.dp, vertical = 10.dp)
            .fillMaxWidth()
            .height(280.dp)
            .clickable { MediaContentUtils.startMedia(trailer) },
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            GlideImage(
                model = trailer.thumbnail,
                contentDescription = trailer.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.55f),
                                Color.Black.copy(alpha = 0.75f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(13.dp)
                    .clip(RoundedCornerShape(100))
                    .background(MainColor)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageIcon(R.drawable.ic_play, 18, Color.White)
            }

            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                TextViewSemiBold(trailer.name.orEmpty().trim(), 18, line = 1)
                Spacer(Modifier.height(1.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextViewNormal(trailer.secId.orEmpty().trim(), 12, line = 1)
                    Spacer(Modifier.width(5.dp))
                    TextViewNormal("•", 15, line = 1)
                    Spacer(Modifier.width(5.dp))
                    TextViewNormal(trailer.extra.orEmpty(), 12, line = 1)
                }
            }
        }
    }
}