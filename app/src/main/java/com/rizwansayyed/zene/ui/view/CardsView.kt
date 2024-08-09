package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.MusicType.*
import com.rizwansayyed.zene.data.api.model.ZeneArtistsPostItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicImportPlaylistsItems
import com.rizwansayyed.zene.data.api.model.ZeneSavedPlaylistsResponseItem
import com.rizwansayyed.zene.service.MusicServiceUtils.openVideoPlayer
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_ARTISTS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MOOD
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PLAYLISTS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_USER_PLAYLISTS
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand
import com.rizwansayyed.zene.utils.Utils.Share.ARTISTS_INNER
import com.rizwansayyed.zene.utils.Utils.Share.PLAYLIST_ALBUM_INNER
import com.rizwansayyed.zene.utils.Utils.Share.SONG_INNER
import com.rizwansayyed.zene.utils.Utils.Share.VIDEO_INNER
import com.rizwansayyed.zene.utils.Utils.Share.WEB_BASE_URL
import com.rizwansayyed.zene.utils.Utils.convertItToMoney
import com.rizwansayyed.zene.utils.Utils.openBrowser
import com.rizwansayyed.zene.utils.Utils.shareTxtImage
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.utils.Utils.ytThumbnail

@Composable
fun SimpleCardsView(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
    var dialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(7.dp)
            .bouncingClickable {
                if (it) openSpecificIntent(m, list)
                else dialog = true
            }) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .width(220.dp)
                .padding(top = 9.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppins(m.name ?: " ", false, size = 16, limit = 1)
        }
    }

    if (dialog) DialogSheetInfos(m) {
        dialog = false
    }
}


@Composable
fun CardsViewDesc(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
    var dialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(7.dp)
            .bouncingClickable {
                if (it) openSpecificIntent(m, list)
                else dialog = true
            }) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .width(220.dp)
                .padding(top = 9.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppins(m.name ?: " ", false, size = 16, limit = 1)
        }

        Row(
            Modifier
                .width(220.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppinsThin(m.artists ?: " ", false, size = 14, limit = 1)
        }
    }

    if (dialog) DialogSheetInfos(m) {
        dialog = false
    }
}


@Composable
fun VideoCardsViewWithSong(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
    var dialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(7.dp)
            .bouncingClickable {
                if (it) openSpecificIntent(m, list)
                else dialog = true
            }) {
        Box(Modifier.size(width = 280.dp, height = 170.dp)) {
            AsyncImage(
                imgBuilder(ytThumbnail(m.extra ?: "")),
                m.name,
                Modifier
                    .align(Alignment.Center)
                    .size(width = 280.dp, height = 170.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )

            ImageView(
                R.drawable.ic_youtube,
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .size(20.dp)
            )
        }

        Row(
            Modifier
                .width(280.dp)
                .padding(top = 9.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppins(m.name ?: " ", false, size = 16, limit = 1)
        }

        Row(
            Modifier
                .width(280.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppinsThin(m.artists ?: " ", false, size = 14, limit = 1)
        }
    }


    if (dialog) DialogSheetInfos(m) {
        dialog = false
    }
}


@Composable
fun CardRoundTextOnly(m: ZeneMusicDataItems) {
    Row(
        Modifier
            .padding(6.dp)
            .width(200.dp)
            .clip(RoundedCornerShape(10))
            .background(MainColor)
            .bouncingClickable { openSpecificIntent(m, emptyList()) },
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        TextPoppins(m.name ?: "", true, size = 16, limit = 2)
    }
}

@Composable
fun CardSmallWithListeningNumber(m: ZeneMusicDataItems, list: ZeneMusicDataResponse) {
    var dialog by remember { mutableStateOf(false) }

    val listeners = stringResource(R.string.listeners)

    Row(
        Modifier
            .padding(6.dp)
            .width(300.dp)
            .bouncingClickable {
                if (it) openSpecificIntent(m, list)
                else dialog = true
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .weight(1f)
                .padding(start = 9.dp, end = 4.dp)
        ) {
            TextPoppins(m.name ?: "", false, size = 16, limit = 1)
            Spacer(Modifier.height(4.dp))
            TextPoppinsThin(m.artists ?: "", false, size = 16, limit = 1)
            Spacer(Modifier.height(4.dp))
            Row(Modifier, Arrangement.Center) {
                Box(Modifier.offset(y = 4.dp)) {
                    ImageIcon(R.drawable.ic_play, 15)
                }
                Spacer(Modifier.width(4.dp))
                TextPoppinsThin(
                    "${convertItToMoney(m.extra ?: "")} $listeners", size = 14, limit = 1
                )
            }
        }
    }


    if (dialog) DialogSheetInfos(m) {
        dialog = false
    }
}

@Composable
fun ArtistsCardView(m: ZeneMusicDataItems, list: ZeneMusicDataResponse) {
    var dialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(7.dp)
            .bouncingClickable {
                if (it) openSpecificIntent(m, list)
                else dialog = true
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 5.dp)
                .width(170.dp)
        ) {
            TextPoppinsThin(m.name ?: "", true, size = 17, limit = 1)
        }
    }

    if (dialog) DialogSheetInfos(m) {
        dialog = false
    }
}


@Composable
fun SongDynamicCards(m: ZeneMusicDataItems, list: ZeneMusicDataResponse) {
    var dialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(bottom = 15.dp)
            .padding(7.dp)
            .bouncingClickable {
                if (it) openSpecificIntent(m, list)
                else dialog = true
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 2.dp)
        ) {
            TextPoppins(m.name ?: "", true, size = 17, limit = 2)
        }

        if (m.type() != PLAYLIST) Row(
            Modifier
                .padding(top = 2.dp)
                .padding(horizontal = 2.dp)
        ) {
            TextPoppinsThin(m.artists ?: "", true, size = 15, limit = 2)
        }
    }


    if (dialog) DialogSheetInfos(m) {
        dialog = false
    }
}

@Composable
fun PlaylistsDynamicCards(m: ZeneSavedPlaylistsResponseItem) {
    var dialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(bottom = 15.dp)
            .padding(7.dp)
            .bouncingClickable {
                if (it) {
                    if (m.isSaved == true) {
                        m.id?.let { sendNavCommand(NAV_PLAYLISTS.replace("{id}", it)) }
                    } else {
                        m.id?.let { sendNavCommand(NAV_USER_PLAYLISTS.replace("{id}", it)) }
                    }
                } else dialog = true
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            imgBuilder(m.img),
            m.name,
            Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 2.dp)
        ) {
            TextPoppins(m.name ?: "", true, size = 17, limit = 2)
        }
    }
}

@Composable
fun NewsItemCard(news: ZeneMusicDataItems) {
    Column(
        Modifier
            .width(300.dp)
            .padding(horizontal = 9.dp)
            .clickable { openSpecificIntent(news, emptyList()) }) {
        AsyncImage(
            imgBuilder(news.thumbnail),
            news.name,
            Modifier
                .clip(RoundedCornerShape(13.dp))
                .size(300.dp, 400.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(10.dp))

        TextPoppins(news.name ?: "", false, size = 15, limit = 3)

        Spacer(Modifier.height(6.dp))

        TextPoppinsThin(
            "${news.getDomain()} • ${news.extra}", false, size = 14, limit = 3
        )
    }
}

@Composable
fun FeedNewsItemView(posts: ZeneArtistsPostItems?) {
    Row(
        Modifier
            .padding(horizontal = 0.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable {
                posts?.url?.let { openBrowser(it) }
            }
            .padding(9.dp), Arrangement.Center, Alignment.CenterVertically) {
        AsyncImage(
            imgBuilder(posts?.thumbnail),
            posts?.name,
            Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            TextPoppinsThin("${posts?.name}'s ${stringResource(R.string.news)}", size = 14)
            Spacer(Modifier.height(5.dp))
            TextPoppins(posts?.caption ?: "", size = 15, limit = 3)

            Row {
                TextPoppinsThin(posts?.timestampToDate() ?: "", size = 14)
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSheetInfos(m: ZeneMusicDataItems, close: () -> Unit) {
    ModalBottomSheet(
        close, Modifier.fillMaxHeight(), containerColor = MainColor, contentColor = MainColor
    ) {
        Column(Modifier.fillMaxSize()) {
            TopInfoItemSheet(m)

            Spacer(Modifier.height(40.dp))

            if (m.type() == SONGS) {
                SongInfoItemSheet(m, close)
            } else if (m.type() == ARTISTS) {
                println()
            } else if (m.type() == PLAYLIST || m.type() == ALBUMS) {
                AlbumPlaylistInfoItemSheet(m, close)
            } else if (m.type() == VIDEO) {
                SheetDialogSheet(R.drawable.ic_play, R.string.play) {
                    openSpecificIntent(m, listOf(m))
                    close()
                }
            } else LaunchedEffect(Unit) {
                close()
                openSpecificIntent(m, listOf(m))
            }


            Spacer(Modifier.height(20.dp))

            SheetDialogSheet(R.drawable.ic_share, R.string.share) {
                shareTxtImage(shareUrl(m))
                close()
            }

            Spacer(Modifier.height(70.dp))
        }

        LaunchedEffect(Unit) {
            logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_SONG_SHEET)
        }
    }
}

@Composable
fun HorizontalCardItems(items: ZeneMusicImportPlaylistsItems) {
//    Row(
//        Modifier
//            .fillMaxSize()
//            .padding(horizontal = 4.dp, vertical = 10.dp),
//        Arrangement.Center, Alignment.CenterVertically
//    ) {
//        AsyncImage(
//            imgBuilder(items.thumbnail),
//            posts?.name,
//            Modifier
//                .size(120.dp)
//                .clip(RoundedCornerShape(14.dp))
//                .background(Color.Gray),
//            contentScale = ContentScale.Crop
//        )
//    }
}

@Composable
fun SheetDialogSheet(icon: Int, txt: Int, close: () -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 14.dp, vertical = 3.dp)
            .fillMaxWidth()
            .clickable { close() }) {
        ImageIcon(icon, 20)

        Spacer(Modifier.width(9.dp))

        TextPoppins(v = stringResource(txt), size = 16)
    }
}


fun shareUrl(m: ZeneMusicDataItems): String {
    return when (m.type()) {
        SONGS -> {
            logEvents(FirebaseLogEvents.FirebaseEvents.SHARE_SONG_URL)
            "${WEB_BASE_URL}${SONG_INNER}${EncodeDecodeGlobal.encryptData(m.id ?: "-")}".trim()
                .replace("\n", "")
        }

        PLAYLIST, ALBUMS -> {
            logEvents(FirebaseLogEvents.FirebaseEvents.SHARE_PLAYLISTS_URL)
            "${WEB_BASE_URL}${PLAYLIST_ALBUM_INNER}${EncodeDecodeGlobal.encryptData(m.id ?: "-")}".trim()
                .replace("\n", "")
        }

        ARTISTS -> {
            logEvents(FirebaseLogEvents.FirebaseEvents.SHARE_ARTISTS_URL)
            "${WEB_BASE_URL}${ARTISTS_INNER}${EncodeDecodeGlobal.encryptData(m.name ?: "-")}".trim()
                .replace("\n", "")
        }

        VIDEO -> {
            logEvents(FirebaseLogEvents.FirebaseEvents.SHARE_VIDEO_URL)
            "${WEB_BASE_URL}${VIDEO_INNER}${EncodeDecodeGlobal.encryptData(m.id ?: "-")}".trim()
                .replace("\n", "")
        }

        MOOD -> WEB_BASE_URL
        STORE -> WEB_BASE_URL
        NEWS -> WEB_BASE_URL
        NONE -> WEB_BASE_URL
    }
}

fun openSpecificIntent(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
    when (m.type()) {
        SONGS -> sendWebViewCommand(m, list)
        PLAYLIST -> m.id?.let { sendNavCommand(NAV_PLAYLISTS.replace("{id}", it)) }
        ALBUMS -> m.id?.let { sendNavCommand(NAV_PLAYLISTS.replace("{id}", it)) }
        ARTISTS -> m.name?.let { sendNavCommand(NAV_ARTISTS.replace("{id}", it)) }
        VIDEO -> openVideoPlayer(m.extra)
        MOOD -> m.id?.let { sendNavCommand(NAV_MOOD.replace("{id}", it)) }
        STORE -> m.id?.let { openBrowser(it) }
        NONE -> {}
        NEWS -> m.id?.let { openBrowser(it) }
    }
}