package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.rizwansayyed.zene.data.model.EntertainmentDiscoverResponse
import com.rizwansayyed.zene.data.model.WhoDatedWhoData
import com.rizwansayyed.zene.ui.theme.LoveBuzzBg
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold


@Composable
fun EntCelebDatingView(data: EntertainmentDiscoverResponse) {
    Spacer(Modifier.height(50.dp))
    Box(Modifier.padding(horizontal = 6.dp)) {
        TextViewBold(stringResource(R.string.love_buzz), 23)
    }
    Spacer(Modifier.height(12.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(data.dated ?: emptyList()) { dated ->
            CoupleCard(dated)
        }
    }
}

@Composable
fun CoupleCard(dated: WhoDatedWhoData) {
    var fullInfoSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .background(LoveBuzzBg)
            .clickable {
                fullInfoSheet = true
            }
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OverlappingAvatars(dated)

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.widthIn(max = 100.dp)) {
                TextViewSemiBold("${dated.coupleComparison?.personA?.name?.trim()}", 14, line = 1)
            }
            TextViewSemiBold("  &  ", size = 14)
            Row(Modifier.widthIn(max = 100.dp)) {
                TextViewSemiBold("${dated.coupleComparison?.personB?.name?.trim()}", size = 14, line = 1)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OverlappingAvatars(data: WhoDatedWhoData) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-7).dp)
        ) {
            GlideImage(
                model = data.coupleComparison?.personA?.image ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            GlideImage(
                model = data.coupleComparison?.personB?.image ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .size(26.dp)
                .background(data.relationshipBadge()?.color ?: Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            ImageIcon(data.relationshipBadge()?.icon ?: R.drawable.ic_romance_couple, 20,Color.Black)
        }
    }
}