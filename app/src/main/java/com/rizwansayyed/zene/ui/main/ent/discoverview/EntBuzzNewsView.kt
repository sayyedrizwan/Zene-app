package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
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

data class NewsItem(
    val category: String,
    val time: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val categoryColor: Color
)

val sampleNews = listOf(
    NewsItem(
        "FILM",
        "12m ago",
        "Timothée Chalamet signed for new biopic role",
        "The actor will portray a legendary musician...",
        "https://example.com/image1.jpg",
        Color(0xFF4DA6FF)
    ),
    NewsItem(
        "GOSSIP",
        "45m ago",
        "Kendall Jenner spotted with Bad Bunny in NYC",
        "Fans speculate romance rumors are true...",
        "https://example.com/image2.jpg",
        Color(0xFFFF6FAE)
    ),
    NewsItem(
        "MUSIC",
        "1h ago",
        "Taylor Swift breaks another streaming record",
        "The Eras Tour film continues to dominate...",
        "https://example.com/image3.jpg",
        Color(0xFFB388FF)
    )
)
//
//@Composable
//fun EntBuzzNewsView() {
//
//}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsRow(item: NewsItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CategoryChip(item.category, item.categoryColor)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.time,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.subtitle,
                fontSize = 13.sp,
                color = Color.LightGray,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        GlideImage(
            item.imageUrl, "",
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun CategoryChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(
                color.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun ViewAllButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF2C1A1A))
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "View All News",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
