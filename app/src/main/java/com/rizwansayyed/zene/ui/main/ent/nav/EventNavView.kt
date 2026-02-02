package com.rizwansayyed.zene.ui.main.ent.nav

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun EventNavView(id: String) {
    val scrollState = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            HeaderSection()

            EventInfoCard()

            ActionButtons()

            ConcertGuideSection()

            UpcomingConcertsSection()

            Spacer(modifier = Modifier.height(224.dp))
        }

        GetTicketsButton(Modifier.align(Alignment.BottomCenter))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HeaderSection() {
    Box {
        GlideImage(
            "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/92/d0/a2/92d0a247-dd68-d0c0-54a8-f0411b8cc935/pr_source.png/840x840bb.webp",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )

        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(500.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.35f),
                            Color.Black.copy(alpha = 0.65f),
                            Color.Black.copy(alpha = 0.85f),
                            Color.Black.copy(alpha = 0.95f),
                        )
                    )
                )
        )


        Column(
            Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            TextViewBold("Harshit Vatya", 25)
            TextViewSemiBold("Karan Enclave, Ghaziabad", 14)
        }
    }
}

@Composable
fun EventInfoCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        InfoChip(
            Icons.Default.DateRange, "DATE", "Feb 2, 2026", Modifier
                .weight(1f)
        )
        InfoChip(
            Icons.Default.AccessTime, "STARTS AT", "7:00 PM", Modifier
                .weight(1f)
        )
    }
}

@Composable
fun InfoChip(icon: ImageVector, title: String, value: String, modifier: Modifier) {
    Column(
        modifier
            .padding(10.dp)
            .background(Color(0xFF131B2A), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color.Cyan)
        Spacer(Modifier.height(8.dp))
        Text(title, color = Color.Gray, fontSize = 12.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ActionButtons() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.DarkGray)
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add to Calendar")
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ticket Alerts", color = Color.White, modifier = Modifier.weight(1f))
            Switch(checked = true, onCheckedChange = {})
        }
    }
}


@Composable
fun ConcertGuideSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Concert Guide", color = Color.White, fontSize = 18.sp)
            Text("Open Maps", color = Color.Cyan)
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color(0xFF1A2238), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Map Preview", color = Color.Gray)
        }

        Spacer(Modifier.height(8.dp))
        Text("Karan Enclave", color = Color.White)
        Text("Ghaziabad, Uttar Pradesh, India", color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun UpcomingConcertsSection() {
    Column(modifier = Modifier.padding(start = 16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Upcoming Concerts", color = Color.White, fontSize = 18.sp)
            Text("View All", color = Color.Cyan)
        }

        Spacer(Modifier.height(12.dp))

        LazyRow {
            item {
                UpcomingConcertCard()
            }
        }
    }
}

@Composable
fun UpcomingConcertCard() {
    Column(
        modifier = Modifier
            .width(180.dp)
            .background(Color(0xFF141C2F), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Text("Feb 04, 2026", color = Color.White)
        Text("Karan Enclave, Ghaziabad", color = Color.Gray, fontSize = 12.sp)
    }
}


@Composable
fun GetTicketsButton(modifier: Modifier) {
    Button(
        onClick = {},
        modifier = modifier
            .padding(bottom = 50.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4FC3F7)
        )
    ) {
        Text("GET TICKETS NOW", fontWeight = FontWeight.Bold)
    }
}



