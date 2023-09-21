package com.rizwansayyed.zene.presenter.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.presenter.theme.LightBlack
import com.rizwansayyed.zene.presenter.ui.SongsTitleAndArtists
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.util.UiUtils.toast

@Composable
fun RecentPlayList(list: List<RecentPlayedEntity>?) {
    if (list != null) Column(verticalArrangement = Arrangement.Center) {
        Spacer(Modifier.height(80.dp))

        if (list.isNotEmpty())
            TopInfoWithSeeMore(
                R.string.recent_played, if (list.size >= 6) R.string.see_all else null
            ) {
                "recentplay see all".toast()
            }
    }
}

@Composable
fun RecentPlayItemsShort(song: RecentPlayedEntity) {
    Column(
        Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LightBlack)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(14.dp))

        AsyncImage(
            song.thumbnail,
            "",
            Modifier
                .size(LocalConfiguration.current.screenWidthDp.dp / 4)
                .clip(RoundedCornerShape(50))
        )

        SongsTitleAndArtists(song.name, song.artists, Modifier, true)
    }
}