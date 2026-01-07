package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import com.rizwansayyed.zene.data.model.EntertainmentDiscoverResponse
import com.rizwansayyed.zene.data.model.WhoDatedWhoData
import com.rizwansayyed.zene.ui.theme.LoveBuzzBg
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
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .background(LoveBuzzBg)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OverlappingAvatars(
            dated.coupleComparison?.personA?.image ?: "",
            dated.coupleComparison?.personB?.image ?: ""
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextViewSemiBold(
            "${dated.coupleComparison?.personA?.name} & ${dated.coupleComparison?.personB?.name}",
            size = 14
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OverlappingAvatars(leftImageUrl: String, rightImageUrl: String) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-7).dp)
        ) {
            GlideImage(
                model = leftImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            GlideImage(
                model = rightImageUrl,
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
                .background(Color(0xFFE65C9C), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}