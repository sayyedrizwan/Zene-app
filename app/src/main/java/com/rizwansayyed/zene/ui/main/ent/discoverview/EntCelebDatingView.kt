package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

data class CelebCouple(
    val name: String,
    val status: String,
    val statusColor: Color,
    val leftImageUrl: String,
    val rightImageUrl: String
)

val celebCouples = listOf(
    CelebCouple(
        name = "Timothée & Kylie",
        status = "CONFIRMED",
        statusColor = GossipColors.Confirmed,
        leftImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUEPeFkVhUCjK33wHiF0VwZH9GEbpoPh-gBPb6rf-_sCrkXo2H-Om5ooIXDxjEhp-OWZGPywumlKeRvCXhRjycaM7VmQiJbPFPA-kxCw&s=10",
        rightImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhv1KoF6R9SiyODgX3X5NqXZUUMZZf7TgLrZO49q3HcW2d1n_DJuPtd_-ZYP7OTBfUzL8tMkY9Z09YIcWqvLVSLxquvck6Z2YdXz_TVw&s=10"
    ),
    CelebCouple(
        name = "Taylor & Travis",
        status = "RUMORED",
        statusColor = GossipColors.Rumored,
        leftImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9rMeO7ak69ZrSsIlCHtNfZv2Fie-ki1oYVNpfTPv8FzpNWZyiFtPX68UChyc7pKObJx6lEI4ye1hckQhmC4Iw5Cu8nWGIzUoZaTbkUQ&s=10",
        rightImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSOTJkwVbOUm6UR8mFFkuaEJ9Xe-x0AZ3s_rTOQXQ3DqI7SbiF2pvuMNi0KTEVrDUgFNEWHJkpRTgBxPJrVrx-lieWf0uj1OqScmQ2CXw&s=10"
    ),
    CelebCouple(
        name = "Zendaya & Tom",
        status = "CONFIRMED",
        statusColor = GossipColors.Confirmed,
        leftImageUrl = "https://i.imgur.com/5AAA1.jpg",
        rightImageUrl = "https://i.imgur.com/6BBB2.jpg"
    ),
    CelebCouple(
        name = "Selena & Benny",
        status = "CONFIRMED",
        statusColor = GossipColors.Confirmed,
        leftImageUrl = "https://i.imgur.com/7CCC3.jpg",
        rightImageUrl = "https://i.imgur.com/8DDD4.jpg"
    ),
    CelebCouple(
        name = "Gigi & Bradley",
        status = "RUMORED",
        statusColor = GossipColors.Rumored,
        leftImageUrl = "https://i.imgur.com/9EEE5.jpg",
        rightImageUrl = "https://i.imgur.com/10FFF6.jpg"
    ),
    CelebCouple(
        name = "Dua & Callum",
        status = "RUMORED",
        statusColor = GossipColors.Rumored,
        leftImageUrl = "https://i.imgur.com/11GGG7.jpg",
        rightImageUrl = "https://i.imgur.com/12HHH8.jpg"
    ),
    CelebCouple(
        name = "Kendall & Bad Bunny",
        status = "ON-OFF",
        statusColor = GossipColors.Rumored,
        leftImageUrl = "https://i.imgur.com/13III9.jpg",
        rightImageUrl = "https://i.imgur.com/14JJJ0.jpg"
    )
)


object GossipColors {
    val CardBg = Color(0xFF3A1C26)
    val Confirmed = Color(0xFFB84C7D)
    val Rumored = Color(0xFF7B5EA7)
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFFDDC6CF)
}

@Composable
fun EntCelebDatingView() {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        HeaderOfCeleb()

        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(celebCouples) { couple ->
                CoupleCard(couple)
            }
        }
    }
}

@Composable
fun HeaderOfCeleb() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xFF3A1C26),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color(0xFFE36A9C)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "New Celeb Couples",
            color = GossipColors.TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun CoupleCard(couple: CelebCouple) {
    Column(
        modifier = Modifier
            .width(260.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(GossipColors.CardBg)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OverlappingAvatars(
            leftImageUrl = couple.leftImageUrl,
            rightImageUrl = couple.rightImageUrl
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = couple.name,
            color = GossipColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatusChip(
            text = couple.status,
            color = couple.statusColor
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OverlappingAvatars(
    leftImageUrl: String,
    rightImageUrl: String
) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .width(120.dp),
        contentAlignment = Alignment.Center
    ) {

        // LEFT IMAGE (slightly behind)
        GlideImage(
            model = leftImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterStart)
                .zIndex(0f)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // RIGHT IMAGE (on top)
        GlideImage(
            model = rightImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .offset(x = 28.dp)
                .zIndex(1f)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // HEART BADGE (center-bottom overlap)
        Box(
            modifier = Modifier
                .size(22.dp)
                .offset(x = 14.dp, y = 22.dp)
                .zIndex(2f)
                .background(Color(0xFFE65C9C), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
fun StatusChip(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.25f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

