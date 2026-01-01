package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val DarkBg = Color(0xFF120A0A)
val CardBg = Color(0xFF1C1212)
val AccentRed = Color(0xFFFF5A3C)

val MoviesBlue = Color(0xFF4DA3FF)
val TvPurple = Color(0xFFB48CFF)
val AwardsGold = Color(0xFFFFC44D)
val StreamingGreen = Color(0xFF4DFF9A)
val GossipPink = Color(0xFFFF6EC7)
val DisneyTeal = Color(0xFF3ED6C6)

@Composable
fun EntTrendingTopicsView() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Header()
            Spacer(Modifier.height(20.dp))
            TrendingMainCard()
            Spacer(Modifier.height(20.dp))
            TrendingGrid()
        }
    }
}

@Composable
fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.TrendingUp,
            contentDescription = null,
            tint = AccentRed
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Trending Topics",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TrendingMainCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(AccentRed, Color.Transparent)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "#1 TRENDING",
                color = AccentRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Taylor Swift",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "2.4M posts",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(10.dp)
                .background(AccentRed, CircleShape)
        )
    }
}

@Composable
fun TrendingGrid() {
    Column {
        Row {
            TrendingSmallCard(
                title = "Saltburn",
                subtitle = "Trending worldwide",
                label = "MOVIES",
                labelColor = MoviesBlue,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            TrendingSmallCard(
                title = "Stranger Things",
                subtitle = "New Season Teaser",
                label = "TV SHOWS",
                labelColor = TvPurple,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))

        Row {
            TrendingSmallCard(
                title = "Golden Globes",
                subtitle = "",
                label = "AWARDS",
                labelColor = AwardsGold,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            TrendingSmallCard(
                title = "The Bear",
                subtitle = "",
                label = "STREAMING",
                labelColor = StreamingGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))

        Row {
            TrendingSmallCard(
                title = "Mean Girls",
                subtitle = "",
                label = "GOSSIP",
                labelColor = GossipPink,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            TrendingSmallCard(
                title = "Percy Jackson",
                subtitle = "",
                label = "DISNEY+",
                labelColor = DisneyTeal,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TrendingSmallCard(
    title: String,
    subtitle: String,
    label: String,
    labelColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(CardBg)
            .border(
                1.dp,
                Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(22.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = label,
                color = labelColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            if (subtitle.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }
        }
    }
}
