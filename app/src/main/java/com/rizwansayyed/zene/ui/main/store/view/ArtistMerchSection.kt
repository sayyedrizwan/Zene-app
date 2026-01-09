package com.rizwansayyed.zene.ui.main.store.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R

@Composable
fun ArtistMerchSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

//        SectionHeader(title = "Artist Merch")

        Spacer(Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MerchProductCard(
                    imageRes = "https://m.media-amazon.com/images/I/619w5E8UpbL._SL1500_.jpg",
                    badge = "Official",
                    title = "Tour T-Shirt 2024",
                    subtitle = "Vintage Black Edition",
                    price = "$35.00"
                )
            }
            item {
                MerchProductCard(
                    imageRes = "https://m.media-amazon.com/images/I/51FycUWokTL._SL1500_.jpg",
                    title = "Limited Edition Vinyl",
                    subtitle = "Midnight Blue Pressing",
                    price = "$29.99"
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MerchProductCard(
    imageRes: String,
    title: String,
    subtitle: String,
    price: String,
    badge: String? = null
) {
    Column(
        modifier = Modifier
            .width(190.dp)
            .background(Color(0xFF182238), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {

        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                imageRes,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.8f),
                contentScale = ContentScale.Fit
            )

            badge?.let {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Gray, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
        Text(subtitle, color = Color(0xFF8FA0C8), fontSize = 13.sp)

        Spacer(Modifier.height(8.dp))

        Text(
            text = price,
            color = Color(0xFF5B7CFF),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(10.dp))

        AmazonButton()
    }
}

@Composable
fun AmazonButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(Color(0xFF2A3552), RoundedCornerShape(12.dp))
            .clickable { }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "View on Amazon",
            color = Color.White,
            fontSize = 13.sp
        )
        Spacer(Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Outlined.OpenInNew,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
    }
}


@Composable
fun FeaturedForYouSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Featured for You",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                FeaturedProductCard(
                    imageRes = "https://m.media-amazon.com/images/I/61WFLydWqpL._SL1500_.jpg",
                    title = "Echo Buds Gen 2",
                    price = "$119.99"
                )
            }
            item {
                FeaturedProductCard(
                    imageRes = "https://m.media-amazon.com/images/I/71YVHVhNNkL._SL1500_.jpg",
                    title = "Pro Stream Mic",
                    price = "$149.00"
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeaturedProductCard(
    imageRes: String,
    title: String,
    price: String
) {
    Column(
        modifier = Modifier
            .width(180.dp)
            .background(Color(0xFF182238), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {

        Box(
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth()
                .background(Color(0xFFE6B89C), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            GlideImage(
                imageRes,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.7f),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
        Text(
            price,
            color = Color(0xFF5B7CFF),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}
