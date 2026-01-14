package com.rizwansayyed.zene.ui.main.store.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

data class MerchItem(
    val title: String,
    val subtitle: String,
    val price: String,
    val imageRes: String,
    val isOfficial: Boolean = false
)

val itemsShopLists = listOf(
    MerchItem(
        "Tour T-Shirt 2024 dddndj fjffjnjfn",
        "Vintage Black Edition",
        "$35.00",
        "https://m.media-amazon.com/images/I/51FycUWokTL._SL1500_.jpg",
        isOfficial = true
    ),
    MerchItem(
        "Limited Edition Vinyl",
        "Midnight Blue Pressing",
        "$29.99",
        "https://m.media-amazon.com/images/I/61BrerJ+yML._SL1500_.jpg"
    ),
    MerchItem(
        "Tour T-Shirt 2024 dddndj aaa",
        "Vintage Black Editiona aagahaaj aahbsdd 7yddd",
        "$35.00",
        "https://m.media-amazon.com/images/I/51FycUWokTL._SL1500_.jpg",
        isOfficial = true
    ),
)


@Composable
fun StoreTopDealsView() {
    Spacer(Modifier.height(50.dp))
    Box(Modifier.padding(horizontal = 6.dp)) {
        TextViewBold(stringResource(R.string.top_deals_on_amazon), 23)
    }
    Spacer(Modifier.height(12.dp))

    val maxHeightPx = remember { mutableIntStateOf(0) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 1.dp),
        contentPadding = PaddingValues(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(itemsShopLists) { item ->
            MerchCard(maxHeightPx, item) { }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MerchCard(maxHeightPx: MutableState<Int>, item: MerchItem, onClick: () -> Unit) {
    val density = LocalDensity.current
    val minHeightDp = with(density) { maxHeightPx.value.toDp() }

    Column(Modifier
        .width(270.dp)
        .heightIn(min = minHeightDp)
        .onGloballyPositioned {
            val h = it.size.height
            if (h > maxHeightPx.value) {
                maxHeightPx.value = h
            }
        }
        .padding(5.dp)) {

        Box(Modifier.fillMaxWidth(), Alignment.TopEnd) {
            GlideImage(
                model = item.imageRes,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )

            Box(
                Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(80))
                    .background(BlackGray)
                    .padding(horizontal = 13.dp, vertical = 10.dp)
            ) {
                TextViewSemiBold(item.price, 15)
            }

            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(95))
                    .background(BlackGray)
                    .padding(horizontal = 13.dp, vertical = 10.dp)
            ) {
                ImageIcon(R.drawable.ic_link_square, 18)
            }

        }

        Spacer(Modifier.height(10.dp))
        TextViewBold(item.title, 16)
        Spacer(modifier = Modifier.weight(1f))
    }
}
