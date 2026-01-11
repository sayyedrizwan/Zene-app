package com.rizwansayyed.zene.ui.main.store.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

data class MerchItem(
    val title: String,
    val subtitle: String,
    val price: String,
    val imageRes: String,
    val isOfficial: Boolean = false
)

@Composable
fun StoreTopDealsView() {
    val items = listOf(
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

    Spacer(Modifier.height(50.dp))
    Box(Modifier.padding(horizontal = 6.dp)) {
        TextViewBold(stringResource(R.string.top_deals_on_amazon), 23)
    }
    Spacer(Modifier.height(12.dp))

    val maxHeightPx = remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 1.dp),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                MerchCard(maxHeightPx, item) { }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MerchCard(maxHeightPx: MutableState<Int>, item: MerchItem, onClick: () -> Unit) {
    val density = LocalDensity.current
    val minHeightDp = with(density) { maxHeightPx.value.toDp() }

    Column(
        Modifier
            .width(270.dp)
            .heightIn(min = minHeightDp)
            .onGloballyPositioned {
                val h = it.size.height
                if (h > maxHeightPx.value) {
                    maxHeightPx.value = h
                }
            }
            .padding(5.dp)
    ) {

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
        }

        Spacer(Modifier.height(10.dp))

        TextViewBold(item.title, 16)

        Row(Modifier.padding(vertical = 5.dp)) {
            TextViewNormal(item.price, 16)

            Spacer(Modifier.width(5.dp))

            Text(
                text = item.price,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = proximanOverFamily,
                textDecoration = TextDecoration.LineThrough
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2F3A4C)
            )
        ) {
            Text("View on Amazon", fontSize = 14.sp, color = Color.White)
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Outlined.OpenInNew,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
