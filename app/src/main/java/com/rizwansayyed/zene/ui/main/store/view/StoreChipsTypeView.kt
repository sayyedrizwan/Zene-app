package com.rizwansayyed.zene.ui.main.store.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.home.view.TextSimpleCards
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.InstagramColor
import com.rizwansayyed.zene.ui.theme.LuxColor
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.PurpleGrey80
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils

val storeChips = listOf(
    "Explore",
    "Headphones & Earbuds",
    "Speakers",
    "Artist / Movie Merchandise",
    "Turntables & Records",
    "Audio Accessories",
    "Books & Biographies",
    "Smart Home / Lighting",
    "Phones / Cases",
    "Fashion & Beauty",
    "Gifts & Bundles"
)


@Composable
fun StoreChipsTypeView(category: MutableState<String>) {
    Row(
        Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(10.dp))

        storeChips.forEach { chip ->
            TextSimpleCards(chip == category.value, chip) {
                category.value = chip
            }

            Spacer(Modifier.width(5.dp))
        }

        Spacer(Modifier.width(5.dp))
    }

    Spacer(Modifier.height(15.dp))
    SupportArtistCard()
}

@Composable
fun SupportArtistCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(LuxColor)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PurpleGrey80),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = InstagramColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            TextViewSemiBold(stringResource(R.string.store_thank_you_note), 15)
        }
    }
}
