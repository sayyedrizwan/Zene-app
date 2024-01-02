package com.rizwansayyed.zene.presenter.ui.home.feed.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.artistsfeed.ArtistsFeedEntity
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.utils.Utils.getTimeAgo


@Composable
fun FeedNewsItem(feed: ArtistsFeedEntity) {
    val width = LocalConfiguration.current.screenWidthDp / 2

    Column(
        Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .clickable {
                Uri
                    .parse(feed.postId)
                    .customBrowser()
            }
            .padding(10.dp)
    ) {
        Spacer(Modifier.height(5.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            TextThin(v = "${feed.artistsName} ${stringResource(id = R.string.news)}", size = 12)

            Spacer(Modifier.weight(1f))

            feed.timeAdded?.let { TextThin(v = getTimeAgo(it), size = 12) }
        }

        Spacer(Modifier.height(15.dp))

        Row(Modifier.fillMaxWidth()) {
            AsyncImage(
                feed.media, feed.artistsName,
                Modifier
                    .weight(5f)
                    .height(width.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier
                    .weight(9f)
                    .height(width.dp)
                    .padding(5.dp), Arrangement.Center
            ) {
                TextSemiBold(v = feed.title ?: "", singleLine = true, size = 15)
                Spacer(Modifier.height(10.dp))
                TextThin(v = feed.desc ?: "", size = 12)
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun FeedYoutubeItem(feed: ArtistsFeedEntity) {
    val width = LocalConfiguration.current.screenWidthDp / 1.5

    Column(
        Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .clickable {
                Uri
                    .parse("https://www.youtube.com/watch?v=${feed.postId}")
                    .customBrowser()
            }
            .padding(10.dp)
    ) {
        Spacer(Modifier.height(5.dp))

        SmallIcons(icon = R.drawable.ic_youtube, 27, 5)

        Spacer(Modifier.height(9.dp))

        AsyncImage(
            "https://img.youtube.com/vi/${feed.postId}/maxresdefault.jpg", feed.artistsName,
            Modifier
                .fillMaxWidth()
                .height(width.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(20.dp))

        TextSemiBold(v = feed.title ?: "", Modifier.fillMaxWidth(), doCenter = true, size = 13)

        Spacer(Modifier.height(10.dp))

        TextThin(
            v = "${stringResource(R.string.by)}: ${feed.artistsName}",
            Modifier.fillMaxWidth(), doCenter = true, size = 12
        )

        Spacer(Modifier.height(10.dp))

        TextThin(
            v = "${stringResource(R.string.uploaded_on)}: ${getTimeAgo(feed.timeAdded ?: 0L)}",
            Modifier.fillMaxWidth(), doCenter = true, size = 12
        )

        Spacer(Modifier.height(15.dp))
    }


}
