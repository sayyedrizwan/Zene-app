package com.rizwansayyed.zene.presenter.ui.home.artists

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistsEvents() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()


    when (val v = artistsViewModel.artistsEvents) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.upcoming_events, null) {}

            Spacer(
                Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenWidthDp.dp)
                    .clip(RoundedCornerShape(6))
                    .background(shimmerBrush())
            )
        }

        is DataResponse.Success -> {
            if ((v.item?.size ?: 0) > 0) {
                TopInfoWithSeeMore(R.string.upcoming_events, null) {}

                val pager = rememberPagerState(pageCount = { v.item?.size ?: 0 })
                HorizontalPager(pager, Modifier.fillMaxWidth()) { page ->
                    EventsItems(v.item?.get(page)!!, artistsViewModel)
                }

                Spacer(Modifier.height(15.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    for (i in 0 until pager.pageCount) {
                        Column(
                            Modifier
                                .animateContentSize()
                                .padding(bottom = 14.dp)
                                .padding(horizontal = 4.dp)
                                .size(if (pager.currentPage == i) 26.dp else 3.dp, 3.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(if (pager.currentPage == i) Color.Gray else Color.White)
                        ) {}
                    }
                }
            }
        }
    }

}

@Composable
fun EventsItems(events: ArtistsEvents, artist: ArtistsViewModel) {
    Box(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenWidthDp.dp)
            .clip(RoundedCornerShape(6))
            .background(Color.Black)
            .clickable {
                FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_EVENTS)
                Uri
                    .parse(events.link)
                    .customBrowser()
            }
    ) {
        val img: String = if (events.artists.isNotEmpty()) events.artists[0].img else
            when (val v = artist.artistsImage) {
                is DataResponse.Success -> v.item
                else -> ""
            }
        AsyncImage(img, events.eventName, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

        Spacer(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.4f))
        )

        Row(
            Modifier
                .align(Alignment.TopCenter)
                .padding()
                .padding(top = 10.dp, end = 10.dp, start = 10.dp),
            Arrangement.Center, Alignment.CenterVertically
        ) {
            Row {
                events.artists.forEachIndexed { index, a ->
                    if (index > 5) return@forEachIndexed
                    AsyncImage(
                        a.img, a.name,
                        Modifier
                            .offset(x = if (index == 0) 0.dp else (-20 * index).dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(50))
                            .border(1.dp, Color.White, RoundedCornerShape(50))
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Column(
                Modifier
                    .clip(RoundedCornerShape(6))
                    .background(MainColor)
                    .padding(vertical = 5.dp, horizontal = 11.dp),
                Arrangement.Center, Alignment.CenterHorizontally
            ) {
                TextThin(events.date(), size = 14)
                TextSemiBold(events.month(), Modifier.offset(x = 4.dp, y = 0.dp), size = 34)
                TextThin(events.year(), size = 14)
            }
        }

        Column(
            Modifier
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            val size = events.address?.split(",")

            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                Spacer(Modifier.width(9.dp))

                SmallIcons(R.drawable.ic_location, size = 18, p = 0)
                val address =
                    if ((size?.size ?: 0) >= 2) "${size?.get(size.size - 1)}"
                    else events.address ?: ""
                TextRegular(address, Modifier.fillMaxWidth(), singleLine = true)
            }

            TextBold(
                events.eventName ?: "",
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                singleLine = true,
                size = 25
            )
        }
    }
}