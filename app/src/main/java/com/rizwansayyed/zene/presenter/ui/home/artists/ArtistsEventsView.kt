package com.rizwansayyed.zene.presenter.ui.home.artists

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistsEvents() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    TopInfoWithSeeMore(R.string.upcoming_events, null) {}

    when (val v = artistsViewModel.artistsEvents) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> TextThin(v = "Loading")
        is DataResponse.Success -> {
            val pager = rememberPagerState(pageCount = { v.item?.size ?: 0 })
            HorizontalPager(pager, Modifier.fillMaxWidth()) { page ->
                EventsItems(v.item?.get(page)!!)
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

@Composable
fun EventsItems(events: ArtistsEvents) {
    Row(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .wrapContentHeight()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Spacer(Modifier.height(23.dp))

            TextBold(
                events.eventName ?: "",
                Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(), size = 25
            )

            val size = events.address?.split(",")

            if ((size?.size ?: 0) > 3)
                TextThin(
                    "${size?.get(0)}, ${size?.get(size.size - 1)}",
                    Modifier
                        .padding(start = 10.dp, top = 9.dp)
                        .fillMaxWidth(), size = 14
                )
            else
                TextThin(
                    events.address ?: "",
                    Modifier
                        .padding(start = 10.dp, top = 9.dp)
                        .fillMaxWidth(), size = 14
                )

            Spacer(Modifier.weight(1f))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 19.dp)
            ) {
                events.artists.forEach {
                    AsyncImage(
                        it.img, it.name,
                        Modifier
                            .padding(horizontal = 3.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .size(40.dp), contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(23.dp))
        }
        Column(Modifier, Arrangement.Center, Alignment.CenterHorizontally) {
            Column(
                Modifier
                    .padding(vertical = 20.dp, horizontal = 25.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MainColor)
                    .padding(7.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                TextThin(events.date(), size = 17)
                TextSemiBold(events.month(), size = 39)
                TextThin(events.year(), size = 17)
            }


            Button(
                onClick = { Uri.parse(events.link).customBrowser() },
                colors = ButtonDefaults.buttonColors(MainColor)
            ) {
                TextThin(stringResource(R.string.book), color = Color.White)
            }

            Spacer(Modifier.height(13.dp))
        }
    }
}