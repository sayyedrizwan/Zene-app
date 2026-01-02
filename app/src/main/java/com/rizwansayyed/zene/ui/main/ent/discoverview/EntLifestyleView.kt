package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.ui.main.ent.CelebrityImages.HARRY_STYLES
import com.rizwansayyed.zene.ui.main.ent.CelebrityImages.TAYLOR_SWIFT
import com.rizwansayyed.zene.ui.main.ent.CelebrityImages.ZENDAYA

data class CelebrityStyle(
    val imageUrl: String,
    val name: String,
    val event: String
)

val celebs = listOf(
    CelebrityStyle(
        imageUrl = ZENDAYA,
        name = "Bella Hadid",
        event = "Paris Fashion Week"
    ),
    CelebrityStyle(
        imageUrl = TAYLOR_SWIFT,
        name = "Zendaya",
        event = "Met Gala"
    ),
    CelebrityStyle(
        imageUrl = HARRY_STYLES,
        name = "Hailey Bieber",
        event = "Street Style"
    )
)


@Composable
fun EntLifestyleView() {
    val pagerState = rememberPagerState { celebs.size }

    Box(modifier = Modifier.fillMaxWidth().padding(top = 30.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(Modifier.height(20.dp))

            Text(
                text = "CELEBRITY STYLE",
                color = Color.White,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 34.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                CelebrityCard(celebs[page])
            }

            Spacer(Modifier.height(12.dp))

            PagerDots(
                totalDots = celebs.size,
                selectedIndex = pagerState.currentPage
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CelebrityCard(item: CelebrityStyle) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
    ) {
        GlideImage(
            model = item.imageUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).height(500.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = item.name,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun PagerDots(
    totalDots: Int,
    selectedIndex: Int
) {
    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (index == selectedIndex) 8.dp else 6.dp)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) Color.Red else Color.DarkGray)
            )
        }
    }
}
