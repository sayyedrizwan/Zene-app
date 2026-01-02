package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun EntLatestTrailerView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFF2A141A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Movie,
                contentDescription = null,
                tint = Color(0xFFE25555)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "Latest Trailers",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "View All",
            color = Color(0xFFE25555),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { }
        )
    }

    FeaturedTrailerCard(
        imageUrl = "https://img.youtube.com/vi/SBzsqPk8bk4/maxresdefault.jpg",
        title = "Marvel's\nThunderbolts*",
        description = "A group of antiheroes goes on missions for the government."
    )

    TrailerRow()
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FeaturedTrailerCard(
    imageUrl: String,
    title: String,
    description: String,
    onPlayClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(420.dp)
            .clip(RoundedCornerShape(28.dp))
    ) {

        // Background image
        GlideImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.15f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // PREMIERE chip
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "PREMIERE",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {

            Text(
                text = title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(20.dp))

            PlayTrailerButton(onClick = onPlayClick)
        }
    }
}

@Composable
fun PlayTrailerButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = Color(0xFFE25555)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Play Trailer",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrailerThumbnail(
    imageUrl: String,
    title: String
) {
    Column(
        modifier = Modifier.width(120.dp)
    ) {
        Box(
            modifier = Modifier
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {

            GlideImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 13.sp,
            maxLines = 1
        )
    }
}

@Composable
fun TrailerRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            TrailerThumbnail(
                imageUrl = "https://images.justwatch.com/poster/340126450/s332/dhurandhar-2025.avif",
                title = "Challengers"
            )
        }
        item {
            TrailerThumbnail(
                imageUrl = "https://images.justwatch.com/poster/321587622/s332/from.avif",
                title = "Dune: Part Two"
            )
        }
        item {
            TrailerThumbnail(
                imageUrl = "https://images.justwatch.com/poster/255390803/s332/demon-slayer-kimetsu-no-yaiba.avif",
                title = "Civil War"
            )
        }

        item {
            TrailerThumbnail(
                imageUrl = "https://images.justwatch.com/poster/301078631/s332/wednesday.avif",
                title = "Civil War"
            )
        }

        item {
            TrailerThumbnail(
                imageUrl = "https://images.justwatch.com/poster/336772986/s332/all-her-fault.avifhttps://images.justwatch.com/poster/336772986/s332/all-her-fault.avif",
                title = "Civil War"
            )
        }
    }
}