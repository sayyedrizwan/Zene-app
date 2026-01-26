package com.rizwansayyed.zene.ui.main.ent.view

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.EventsResponses
import com.rizwansayyed.zene.data.model.EventsResponsesItems
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.URLSUtils.getSearchNewsOnGoogle
import com.rizwansayyed.zene.utils.share.MediaContentUtils

@Composable
fun EntertainmentNearByEventsView(events: EventsResponses) {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(20.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.events_near_you_this_week), 23)
        }
        Spacer(Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(events.thisWeek ?: emptyList()) {
                EntertainmentNearByEventsItemView(it)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntertainmentNearByEventsItemView(event: EventsResponsesItems) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .height(400.dp)
            .width(300.dp)
            .fillMaxWidth()
    ) {
        GlideImage(
            event.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f),
                            Color.Black.copy(alpha = 0.85f),
                            Color.Black.copy(alpha = 0.95f)
                        ),
                        startY = 300f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier
                        .size(64.dp)
                        .background(
                            Color.Black.copy(alpha = 0.80f), RoundedCornerShape(16.dp)
                        ),
                    Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    TextViewSemiBold(event.getDay(), 18)
                    TextViewSemiBold(event.getMonth(), 15)
                }

                Spacer(Modifier.weight(1f))

                Column(
                    Modifier
                        .size(64.dp)
                        .clickable {
                            MediaContentUtils.openCustomBrowser(event.ticket)
                        }
                        .background(
                            Color.Black.copy(alpha = 0.80f), RoundedCornerShape(16.dp)
                        ),
                    Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    ImageIcon(R.drawable.ic_ticket, 33, null)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            TextViewBold(event.name.orEmpty(), 20)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                ImageIcon(R.drawable.ic_location, 15, Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                TextViewNormal(event.address.orEmpty(), 16, line = 1)
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}