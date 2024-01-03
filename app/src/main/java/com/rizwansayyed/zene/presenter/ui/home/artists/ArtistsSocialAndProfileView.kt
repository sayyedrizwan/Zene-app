package com.rizwansayyed.zene.presenter.ui.home.artists

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileResponseItem
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.utils.Utils.formatNumberToFollowers
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import java.net.URL



private fun getDomain(url: String): String {
    return try {
        val uri = URL(url)
        val host = uri.host
        if (host.startsWith("www.")) {
            host.substring(4)
        } else {
            host
        }
    } catch (e: Exception) {
        ""
    }
}

fun String?.slash(): String {
    if ((this?.substringAfterLast("/") ?: "").length <= 2) {
        val lastSlashIndex = this?.lastIndexOf('/') ?: 0
        val secondLastSlashIndex = this?.lastIndexOf('/', lastSlashIndex - 1) ?: 0

        return if (secondLastSlashIndex >= 0) {
            val result = this?.substring(secondLastSlashIndex + 1)
            result?.replace("/", "") ?: ""
        } else this?.substringAfterLast("/") ?: ""
    }
    return this?.substringAfterLast("/") ?: ""
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArtistsSocialMedia() {
    val artists: ArtistsViewModel = hiltViewModel()

    when (val v = artists.artistSocialProfile) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> ArtistsSocialMediaLoading()
        is DataResponse.Success -> FlowRow {
            v.item.social.forEachIndexed { index, social ->
                if (index == 0) {
                    val d = getDomain(social.url ?: "")
                    ArtistsSocialMediaButton(
                        R.drawable.ic_internet, d, stringResource(R.string.website)
                    ) {
                        FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_WEBSITE)
                        Uri.parse(social.url).customBrowser()
                    }
                    return@forEachIndexed
                }
                ArtistsSocialMediaList(social)
            }
        }
    }
}

@Composable
fun ArtistsSocialMediaList(social: SoundCloudProfileResponseItem) {
    when {
        social.url?.lowercase()?.contains("facebook.com") == true -> ArtistsSocialMediaButton(
            R.drawable.ic_facebook, "@${social.url.slash()}", stringResource(R.string.facebook)
        ) {
            FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_FACEBOOK)
            Uri.parse(social.url).customBrowser()
        }

        social.url?.lowercase()?.contains("twitter.com") == true -> ArtistsSocialMediaButton(
            R.drawable.ic_twitter, "@${social.url.slash()}", stringResource(R.string.twitter)
        ) {
            FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_TWITTER)
            Uri.parse(social.url).customBrowser()
        }

        social.url?.lowercase()?.contains("instagram.com") == true -> ArtistsSocialMediaButton(
            R.drawable.ic_instagram, "@${social.url.slash()}", stringResource(R.string.instagram)
        ) {
            FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_INSTAGRAM)
            Uri.parse(social.url).customBrowser()
        }

        social.url?.lowercase()?.contains("youtube.com") == true -> ArtistsSocialMediaButton(
            R.drawable.ic_youtube, "/${social.url.slash()}", stringResource(R.string.youtube)
        ) {
            FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_YOUTUBE)
            Uri.parse(social.url).customBrowser()
        }

        social.url?.lowercase()?.contains("snapchat.com") == true -> ArtistsSocialMediaButton(
            R.drawable.ic_snapchat, "@${social.url.slash()}", stringResource(R.string.snapchat)
        ) {
            FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_SNAPCHAT)
            Uri.parse(social.url).customBrowser()
        }

        social.url?.lowercase()?.contains("tiktok.com") == true -> ArtistsSocialMediaButton(
            R.drawable.ic_tiktok, social.url.slash(), stringResource(R.string.tiktok)
        ) {
            FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_TIKTOK)
            Uri.parse(social.url).customBrowser()
        }

        social.url?.lowercase()?.contains("/store") == true -> ArtistsSocialMediaButton(
            R.drawable.store_with_bag, getDomain(social.url), stringResource(R.string.store)
        ) {
            FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_ARTISTS_STORE)
            Uri.parse(social.url).customBrowser()
        }
    }
}

@Composable
fun ArtistsSocialMediaLoading() {

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        Spacer(
            Modifier
                .padding(5.dp)
                .height(95.dp)
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(shimmerBrush())
        )

        Spacer(
            Modifier
                .padding(5.dp)
                .height(95.dp)
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(shimmerBrush())
        )
    }
}

@Composable
fun ArtistsSocialMediaButton(img: Int, s: String, t: String, click: () -> Unit) {
    Box(
        Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp / 2)
            .padding(5.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .clickable { click() }
    ) {
        Column(
            Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 7.dp)
        ) {
            Spacer(Modifier.height(27.dp))
            TextThin(t, size = 14)
            Spacer(Modifier.height(15.dp))
            TextThin(s, size = 12)
            Spacer(Modifier.height(27.dp))
        }

        Image(
            painterResource(img), "",
            Modifier
                .padding(10.dp)
                .size(25.dp)
                .align(Alignment.BottomEnd),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}


@Composable
fun ArtistsProfilePin() {
    val artists: ArtistsViewModel = hiltViewModel()

    when (val v = artists.artistSocialProfile) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {}
        is DataResponse.Success -> {
            if ((v.item.followersCount ?: 0) > 0) {
                val s = formatNumberToFollowers(v.item.followersCount ?: 0)
                TextBold(
                    String.format(stringResource(R.string.pinned_by_users), s),
                    Modifier.fillMaxWidth(), doCenter = true
                )
            }
        }
    }
}