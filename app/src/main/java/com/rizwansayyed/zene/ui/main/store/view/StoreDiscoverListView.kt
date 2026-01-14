package com.rizwansayyed.zene.ui.main.store.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.rizwansayyed.zene.ui.main.ent.discoverview.PagerDots
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBold

@Composable
fun StoreDiscoverListView() {
    Spacer(Modifier.height(50.dp))
    Box(Modifier.padding(horizontal = 6.dp)) {
        TextViewBold(stringResource(R.string.products_you_may_like), 23)
    }
    Spacer(Modifier.height(12.dp))
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecommendedItemCard(item: MerchItem, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .padding(10.dp)
            .clickable { onClick() }
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MainColor)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val pagerState = rememberPagerState(pageCount = { 6 })

        Box(Modifier.size(140.dp)) {
            HorizontalPager(
                pagerState, Modifier.clip(RoundedCornerShape(12.dp))
            ) { page ->
                GlideImage(
                    item.imageRes,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                Modifier
                    .padding(bottom = 5.dp)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(50))
                    .background(BlackTransparent)
                    .padding(horizontal = 5.dp)
            ) {
                PagerDotsSmall(6, pagerState.currentPage)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))


        Column(Modifier.weight(1f), Arrangement.Center) {
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.subtitle,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )


            Text(
                text = item.price,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun PagerDotsSmall(totalDots: Int, selectedIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .size(if (index == selectedIndex) 4.dp else 3.dp)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) MainColor else Color.DarkGray)
            )
        }
    }
}
