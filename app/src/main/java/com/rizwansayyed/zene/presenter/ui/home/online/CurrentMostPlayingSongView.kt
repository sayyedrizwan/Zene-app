package com.rizwansayyed.zene.presenter.ui.home.online

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBoldBig
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.presenter.util.UiUtils.convertMoney
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrentMostPlayingSong() {
    val context = LocalContext.current as Activity
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()
    val homeNavModel: HomeNavViewModel = hiltViewModel()

    val player = ExoPlayer.Builder(context).build()

    when (val v = homeApiViewModel.mostPlayingSong) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            Column(Modifier.fillMaxWidth()) {
                MostPlayingText()

                Spacer(Modifier.height(20.dp))

                MostPlayedSongsLoading()
            }
        }

        is DataResponse.Success -> {
            Column(Modifier.fillMaxWidth()) {
                MostPlayingText()

                Spacer(Modifier.height(20.dp))

                LazyRow(Modifier.fillMaxWidth()) {
                    items(v.item ?: emptyList(), { it.pId ?: (2..33).random().toString() }) {
                        MostPlayedSongView(
                            it,
                            Modifier
                                .animateItemPlacement()
                                .clickable {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        "running".toast()
                                        val mediaItem = MediaItem
                                            .Builder()
                                            .setMediaId(it.pId!!)
                                            .setUri("https://www.pagalwrold.com/siteuploads/files/sfd34/16974/Jo%20Dhaga%20Tumse%20Jud%20Gaya%20Wafa%20Ka_320(PagalWorld).mp3")
                                            .setMediaMetadata(
                                                MediaMetadata.Builder()
                                                    .setTitle(it?.name)
                                                    .setArtist(it?.artists)
                                                    .setArtworkUri(it.thumbnail?.toUri())
                                                    .build()
                                            )
                                            .build()
                                        player.setMediaItem(mediaItem, true)
                                        player.playWhenReady = true
                                        player.prepare()
                                        player.play()
                                    }
                                }, homeNavModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MostPlayedSongsLoading() {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        repeat(5) {
            Column(
                Modifier
                    .padding(horizontal = 12.dp)
                    .width((LocalConfiguration.current.screenWidthDp / 1.7).dp)
                    .height((LocalConfiguration.current.screenWidthDp / 1.2).dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(shimmerBrush())
            ) {}
        }
    }
}

@Composable
fun MostPlayedSongView(music: MusicDataWithArtists, modifier: Modifier, homeNav: HomeNavViewModel) {
    Box(
        modifier
            .padding(horizontal = 12.dp)
            .width((LocalConfiguration.current.screenWidthDp / 1.7).dp)
            .height((LocalConfiguration.current.screenWidthDp / 1.2).dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            music.artistsImg,
            music.artistsName,
            Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Spacer(
            Modifier
                .fillMaxSize()
                .background(MainColor.copy(0.3f))
        )

        Column {
            TextThin(
                music.artists ?: "",
                Modifier
                    .padding(horizontal = 6.dp)
                    .padding(top = 5.dp),
                size = 14
            )

            TextSemiBold(
                music.name ?: "",
                Modifier
                    .padding(horizontal = 6.dp)
                    .padding(top = 5.dp),
                size = 22
            )

            Row(
                Modifier
                    .padding(horizontal = 6.dp)
                    .padding(top = 5.dp), Arrangement.Center, Alignment.CenterVertically
            ) {
                SmallIcons(R.drawable.ic_play, 20, 0) {}

                TextRegular(
                    "${music.listeners?.convertMoney()} ${stringResource(R.string.listeners)}",
                    Modifier.padding(horizontal = 2.dp),
                    size = 14
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(11.dp)
                .align(Alignment.BottomCenter), Arrangement.Center, Alignment.CenterVertically
        ) {
            MenuIcon {
                val m = MusicData(
                    music.thumbnail, music.name, music.artistsName, music.pId, MusicType.MUSIC
                )
                homeNav.setSongDetailsDialog(m)
            }

            Spacer(Modifier.weight(1f))

            AsyncImage(
                music.thumbnail,
                music.name,
                Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(50))
            )
        }
    }
}

@Composable
fun MostPlayingText() {
    TextBoldBig(
        stringResource(id = R.string.zene_mostly_most_played).substringBefore("\n"),
        Modifier
            .padding(start = 14.dp)
            .fillMaxWidth(),
        size = 25
    )

    Spacer(Modifier.height(2.dp))

    TextBoldBig(
        stringResource(id = R.string.zene_mostly_most_played).substringAfter("\n"),
        Modifier
            .padding(start = 14.dp)
            .fillMaxWidth(),
        size = 25
    )
}