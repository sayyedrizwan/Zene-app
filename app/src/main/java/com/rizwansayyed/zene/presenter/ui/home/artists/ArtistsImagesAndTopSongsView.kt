package com.rizwansayyed.zene.presenter.ui.home.artists

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.soundcloud.SocialMediaAccounts
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import kotlin.math.absoluteValue

private val socialMedia = listOf(
    SocialMediaAccounts(
        R.drawable.ic_internet, context.resources.getString(R.string.website), "website"
    ),
    SocialMediaAccounts(
        R.drawable.ic_instagram, context.resources.getString(R.string.instagram), "instagram"
    ),
    SocialMediaAccounts(
        R.drawable.ic_facebook, context.resources.getString(R.string.facebook), "facebook"
    ),
    SocialMediaAccounts(
        R.drawable.ic_twitter, context.resources.getString(R.string.facebook), "twitter"
    ),
    SocialMediaAccounts(
        R.drawable.ic_youtube, context.resources.getString(R.string.facebook), "youtube"
    ),
    SocialMediaAccounts(
        R.drawable.ic_snapchat, context.resources.getString(R.string.facebook), "snapchat"
    ),
    SocialMediaAccounts(
        R.drawable.store_with_bag, context.resources.getString(R.string.facebook), "merch store"
    ),
    SocialMediaAccounts(
        R.drawable.ic_atomic, context.resources.getString(R.string.facebook), "bandpage"
    )
)

@Composable
fun ArtistsImagesView() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    when (val v = artistsViewModel.artistsImages) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.artist_photos, null) {}

            ArtistPhotoAlbum(listOf("", "", "", "", "", "", "", "", "", ""), true)
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.artist_photos, null) {}

            ArtistPhotoAlbum(v.item, false)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArtistPhotoAlbum(item: List<String>, isLoading: Boolean) {
    val height = LocalConfiguration.current.screenHeightDp.dp / 2
    val width = (LocalConfiguration.current.screenWidthDp.absoluteValue / 1.3).toInt()
    val paddingReminder = (LocalConfiguration.current.screenWidthDp.absoluteValue - width) / 2
    val pagerState = rememberPagerState(pageCount = { item.size })


    HorizontalPager(
        pagerState, Modifier.fillMaxWidth(), PaddingValues(horizontal = paddingReminder.dp)
    ) { page ->
        Card({},
            Modifier
                .padding(top = if (page == pagerState.currentPage) 0.dp else 60.dp)
                .padding(7.dp)
                .size(width.dp, height)
                .graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                }
        ) {
            AsyncImage(
                item[page],
                "",
                if (isLoading) Modifier
                    .background(shimmerBrush())
                    .fillMaxSize()
                else Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(item.size / 2)
    }
}

@Composable
fun ArtistsSocialMediaProfile() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    ArtistsProfileLoading()

//    when (val v = artistsViewModel.artistSocialProfile) {
//        DataResponse.Empty -> {}
//        is DataResponse.Error -> {}
//        DataResponse.Loading -> ArtistsProfileLoading()
//        is DataResponse.Success -> {}
//    }
}


@Composable
fun ArtistsProfileLoading() {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        socialMedia.forEach {
            Row(
                Modifier
                    .padding(horizontal = 17.dp)
                    .clickable {}
                    .clip(RoundedCornerShape(12.dp))
                    .background(MainColor)
                    .padding(vertical = 10.dp, horizontal = 22.dp),
                Arrangement.Center, Alignment.CenterVertically
            ) {
                SmallIcons(it.icon, 22, 5)

                Spacer(Modifier.width(6.dp))

                TextRegular(stringResource(R.string.pin), size = 16)
            }
        }
    }
}