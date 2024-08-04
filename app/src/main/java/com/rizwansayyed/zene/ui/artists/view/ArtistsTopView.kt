package com.rizwansayyed.zene.ui.artists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.MusicType
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.db.DataStoreManager.pinnedArtistsList
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.view.ButtonWithImage
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.ui.view.shimmerEffectBrush
import com.rizwansayyed.zene.utils.Utils.RADIO_ARTISTS
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun ArtistsTopView(v: ZeneArtistsInfoResponse) {
    var fullDesc by remember { mutableStateOf(false) }

    Column {
        LazyRow(Modifier.fillMaxWidth()) {
            items(v.img ?: emptyList()) {
                ArtistsTopImagesView(it, v.name, false)
            }
        }

        Spacer(Modifier.height(30.dp))
        Row(Modifier.padding(horizontal = 10.dp)) {
            TextPoppins(v.name ?: "", size = 30)
        }
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .clickable { fullDesc = !fullDesc }) {
            TextPoppins(
                v.desc?.trim() ?: "",
                size = 13,
                limit = if (fullDesc) 10000 else 3
            )
        }
    }
}

@Composable
fun FollowArtists(artists: ZeneArtistsInfoResponse, viewModel: ZeneViewModel) {
    var isFollowing by remember { mutableStateOf(false) }
    val canFollowOnly40 = stringResource(R.string.cannot_follow_more_then_40_artists)

    Spacer(Modifier.height(80.dp))

    val a = stringResource(R.string.followed_by)

    if ((artists.followers ?: 0) > 0) {
        TextPoppinsThin("$a ${artists.followers()}", true)
        Spacer(Modifier.height(30.dp))
    }

    ButtonWithImage(
        if (isFollowing) R.drawable.ic_user_check else R.drawable.ic_add_user,
        if (isFollowing) R.string.following else R.string.follow
    ) {
        isFollowing = !isFollowing
        viewModel.followArtists(artists.name, isFollowing) {
            canFollowOnly40.toast()
        }
    }

    Spacer(Modifier.height(40.dp))

    ButtonWithImage(R.drawable.ic_radio, R.string.radio) {
        val m = ZeneMusicDataItems(
            "${artists.name} Radio",
            artists.name ?: "",
            "${artists.radioID}$RADIO_ARTISTS",
            artists.img?.randomOrNull() ?: "",
            "",
            MusicType.SONGS.name
        )
        sendWebViewCommand(m, listOf(m))
    }


    LaunchedEffect(Unit) {
        val isPresent = pinnedArtistsList.firstOrNull()
            ?.filter { it.lowercase() == artists.name?.lowercase() } ?: emptyList()

        isFollowing = isPresent.isNotEmpty()
    }
}

@Composable
fun ArtistsTopViewLoading() {
    Column {
        LazyRow(Modifier.fillMaxWidth()) {
            items(30) {
                ArtistsTopImagesView("", "", true)
            }
        }

        Spacer(Modifier.height(30.dp))
        Spacer(
            Modifier
                .padding(horizontal = 5.dp)
                .size(100.dp, 10.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )
    }
}