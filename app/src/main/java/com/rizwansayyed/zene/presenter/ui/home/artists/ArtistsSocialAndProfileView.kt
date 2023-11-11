package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.domain.soundcloud.SocialMediaAccounts
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.utils.Utils.formatNumberToFollowers
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel


private val socialMedia = listOf(
    SocialMediaAccounts(
        R.drawable.ic_internet,
        ApplicationModule.context.resources.getString(R.string.website),
        "website"
    ),
    SocialMediaAccounts(
        R.drawable.ic_instagram,
        ApplicationModule.context.resources.getString(R.string.instagram),
        "instagram"
    ),
    SocialMediaAccounts(
        R.drawable.ic_facebook,
        ApplicationModule.context.resources.getString(R.string.facebook),
        "facebook"
    ),
    SocialMediaAccounts(
        R.drawable.ic_twitter,
        ApplicationModule.context.resources.getString(R.string.facebook),
        "twitter"
    ),
    SocialMediaAccounts(
        R.drawable.ic_youtube,
        ApplicationModule.context.resources.getString(R.string.facebook),
        "youtube"
    ),
    SocialMediaAccounts(
        R.drawable.ic_snapchat,
        ApplicationModule.context.resources.getString(R.string.facebook),
        "snapchat"
    ),
    SocialMediaAccounts(
        R.drawable.store_with_bag,
        ApplicationModule.context.resources.getString(R.string.facebook),
        "merch store"
    ),
    SocialMediaAccounts(
        R.drawable.ic_atomic,
        ApplicationModule.context.resources.getString(R.string.facebook),
        "bandpage"
    )
)


@Composable
fun ArtistsSocialMedia() {
    ArtistsSocialMediaLoading()
}

@Composable
fun ArtistsSocialMediaLoading() {

}


@Composable
fun ArtistsProfilePin() {
    val artists: ArtistsViewModel = hiltViewModel()

    when (val v = artists.artistSocialProfile) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {}
        is DataResponse.Success -> {
            if ((v.item.profile?.user?.followers_count ?: 0) > 0) {
                val s = formatNumberToFollowers(v.item.profile?.user?.followers_count ?: 0)
                TextBold(
                    String.format(stringResource(R.string.pinned_by_users), s),
                    Modifier.fillMaxWidth(), doCenter = true
                )
            }
        }
    }
}