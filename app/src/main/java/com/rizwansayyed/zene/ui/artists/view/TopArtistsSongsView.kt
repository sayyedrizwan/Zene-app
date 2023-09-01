package com.rizwansayyed.zene.ui.artists.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.model.Post
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.presenter.jsoup.model.NewsResponse
import com.rizwansayyed.zene.ui.artists.model.ArtistsAlbumsData
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.openOnInstagramPost
import com.rizwansayyed.zene.utils.Utils.shortTextForView

@Composable
fun TopArtistsSongs(songs: TopArtistsSongs, search: (String, String) -> Unit) {
    Row(
        Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp - 90.dp)
            .padding(5.dp)
            .clickable {
                search(songs.img ?: "", songs.name ?: "")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = songs.img,
            contentDescription = songs.name,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(Modifier.fillMaxWidth()) {
            QuickSandSemiBold(songs.name ?: "", size = 17, maxLine = 1)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(id = R.drawable.ic_play_icon),
                    "",
                    Modifier
                        .size(21.dp)
                        .padding(3.dp)
                        .offset(x = 0.dp, y = 2.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )

                QuickSandLight(songs.artist?.replace("listeners", "") ?: "", size = 12, maxLine = 1)
            }
        }
    }
}


@Composable
fun ArtistsAllSongsView(artists: TopArtistsSongs, search: (String, String) -> Unit) {
    Column(
        Modifier
            .padding(10.dp)
            .clickable {
                search(artists.name ?: "", artists.img ?: "")
            },
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = artists.img,
            contentDescription = artists.name,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandLight(artists.name?.shortTextForView(22) ?: "", size = 17)
    }
}


@Composable
fun ArtistsAlbum(album: ArtistsAlbumsData) {
    Column(
        Modifier
            .padding(10.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = album.image,
            contentDescription = album.name,
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp - 10.dp)
                .height(LocalConfiguration.current.screenWidthDp.dp / 2)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )

        QuickSandBold(album.name, size = 16, maxLine = 1, modifier = Modifier.padding(top = 10.dp))

        QuickSandLight(
            album.songsSize.substringAfter("listeners"),
            size = 15,
            modifier = Modifier.padding(top = 1.dp, bottom = 10.dp)
        )
    }
}

@Composable
fun InstagramPostsPosts(post: Post) {
    AsyncImage(
        model = post.postImage,
        contentDescription = "instagram",
        modifier = Modifier
            .padding(5.dp)
            .width(270.dp)
            .height(190.dp)
            .clickable {
                openOnInstagramPost(post.postId ?: "/")
            },
        contentScale = ContentScale.Crop
    )
}


@Composable
fun ArtistsNews(news: NewsResponse) {
    Row(
        Modifier
            .padding(bottom = 20.dp, top = 10.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp - 50.dp)
            .padding(10.dp)
            .clickable {
//                    openOnInstagramPost(post.postId ?: "/")
            }
    ) {
        Column(Modifier.weight(1f)) {
            Spacer(modifier = Modifier.height(10.dp))
            QuickSandSemiBold(
                news.title, size = 13, modifier = Modifier.padding(2.dp), align = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(30.dp))

            QuickSandLight(
                "${news.publishedName}   •  ${news.date} ",
                size = 13,
                modifier = Modifier.padding(2.dp),
                align = TextAlign.Start
            )
        }

        AsyncImage(
            model = news.img,
            contentDescription = "",
            modifier = Modifier
                .padding(5.dp)
                .size(LocalConfiguration.current.screenWidthDp.dp / 2 / 2)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}