package com.rizwansayyed.zene.ui.main.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.ui.main.store.view.ArtistMerchSection
import com.rizwansayyed.zene.ui.main.store.view.FeaturedForYouSection

@Composable
fun StoreView() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 250.dp)
    ) {
        item {
            StoreTopBar()
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            StoreCategoryChips()
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            SupportInfoCard()
            Spacer(modifier = Modifier.height(20.dp))

        }

        item {
            FeaturedPicksSection()
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            ArtistMerchSection()
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            FeaturedForYouSection()
        }
    }
}

@Composable
fun StoreTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Store",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StoreCategoryChips() {
    val categories = listOf("All Items", "Vinyls", "Apparel", "Audio")
    var selected by remember { mutableStateOf("All Items") }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { item ->
            FilterChip(
                text = item,
                selected = selected == item,
                onClick = { selected = item }
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (selected) Color(0xFF5B6CFF)
                else Color(0xFF2A324A)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SupportInfoCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A2136),
                        Color(0xFF12192E)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF3A1E2F), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color(0xFFFF5C8A),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = "Support your this app. " +
                    "Every purchase through our Amazon store helps us keep the music playing.",
            color = Color(0xFFCBD2FF),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}


@Composable
fun FeaturedPicksSection() {
    Column {
        Text(
            text = "Featured Picks",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                FeaturedProductCard(
                    tag = "ARTIST MERCH",
                    title = "Neon Dreams\nLimited Hoodie",
                    subtitle = "Summer Tour '24",
                    price = "$65.00",
                    gradient = listOf(
                        Color(0xFFF39A5C),
                        Color(0xFFE96A6A)
                    ),
                    imageRes = "https://png.pngtree.com/png-clipart/20231020/original/pngtree-hoodie-3d-rendering-png-image_13391022.png"
                )
            }

            item {
                FeaturedProductCard(
                    tag = "Taylor Swift MERCH",
                    title = "Yo You \n III",
                    subtitle = "Summer Tour '24",
                    price = "$65.00",
                    gradient = listOf(
                        Color(0xFF1A1515),
                        Color(0xFF1A1212)
                    ),
                    imageRes = "https://i.pinimg.com/564x/65/53/05/65530574067880ca2a4a38ac3c8aa74f.jpg"
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeaturedProductCard(
    tag: String,
    title: String,
    subtitle: String,
    price: String,
    gradient: List<Color>,
    imageRes: String
) {
    Box(
        modifier = Modifier
            .width(260.dp)
            .height(380.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.verticalGradient(gradient)
            )
            .padding(20.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // Tag pill
            Box(
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.25f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = tag,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    imageRes,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = price,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color(0xFFE96A6A)
                    )
                }
            }
        }
    }
}
