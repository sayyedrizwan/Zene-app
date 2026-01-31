package com.rizwansayyed.zene.ui.main.store.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.StoreDealResponse
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils.openCustomBrowser
import com.rizwansayyed.zene.viewmodel.HomeViewModel


@Composable
fun StoreTopDealsView(v: StoreDealResponse, viewModel: HomeViewModel) {
    if (v.items?.isNotEmpty() == true) {
        val maxHeightPx = remember { mutableIntStateOf(0) }

        Spacer(Modifier.height(10.dp))
        Box(Modifier.padding(horizontal = 8.dp)) {
            TextViewSemiBold(v.name.orEmpty(), 20)
        }
        Spacer(Modifier.height(5.dp))

        LazyRow(
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxWidth()
                .heightIn(min = 1.dp),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(v.items) { item ->
                MerchCard(maxHeightPx, item, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MerchCard(
    maxHeightPx: MutableState<Int>,
    item: StoreDealResponse.StoreDealResponseItem,
    viewModel: HomeViewModel
) {
    val density = LocalDensity.current
    val minHeightDp = with(density) { maxHeightPx.value.toDp() }

    Column(Modifier
        .width(270.dp)
        .clickable {
            item.link?.let { viewModel.storeStripeLink(it) }
        }
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
                model = item.image,
                contentDescription = item.title,
                modifier = Modifier
                    .background(Color.Black)
                    .clip(RoundedCornerShape(16.dp))
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
                TextViewSemiBold("${item.discountPercent}% OFF", 15)
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextViewBold("${item.currency}${item.mrp}")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${item.currency}${item.dealPrice}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = proximanOverFamily,
                style = TextStyle(
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )
            )
        }

        Spacer(Modifier.height(5.dp))
        TextViewBold(item.title.orEmpty(), 16, line = 2)
        Spacer(modifier = Modifier.weight(1f))
    }
}
