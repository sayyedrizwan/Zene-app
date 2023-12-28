package com.rizwansayyed.zene.presenter.ui.extra.standby.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.onClickOn3SecDelay
import com.rizwansayyed.zene.service.player.listener.PlayServiceListener
import com.rizwansayyed.zene.service.player.listener.PlayerServiceInterface
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


@Composable
fun StandbySongInfoView(player: ExoPlayer) {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()
    val p by musicPlayerData.collectAsState(initial = runBlocking(Dispatchers.IO) { musicPlayerData.first() })

    Box(Modifier.fillMaxSize()) {
        when (val v = roomDbViewModel.songsSuggestionForUsers) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> {}
            DataResponse.Loading -> LoadingStateBar()
            is DataResponse.Success -> {
                LazyRow(Modifier.fillMaxWidth()) {
                    item {
                        Spacer(Modifier.width(40.dp))
                    }

                    item {
                        SongYouMayLikeText()
                    }

                    itemsIndexed(v.item) { i, m ->
                        StandByExploreItems(m) {
                            addAllPlayer(v.item.toTypedArray(), i)
                        }
                    }

                    item {
                        Spacer(Modifier.width(30.dp))
                    }

                }
            }
        }

        CurrentPlayingSongInfo(Modifier.align(Alignment.BottomCenter), p, player)
    }

    LaunchedEffect(Unit) {
        roomDbViewModel.init()
    }
}

@Composable
fun CurrentPlayingSongInfo(modifier: Modifier, p: MusicPlayerData?, player: ExoPlayer) {
    val width = (LocalConfiguration.current.screenWidthDp / 5).dp

    var isLoading by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    Row(
        modifier
            .padding(bottom = 4.dp, start = 15.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            p?.v?.thumbnail, p?.v?.songName, Modifier
                .clip(RoundedCornerShape(2))
                .size(width)
        )

        Column(Modifier.padding(start = 4.dp)) {
            TextSemiBold(v = p?.v?.songName ?: "", singleLine = true, size = 15)

            Spacer(Modifier.height(30.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
                if (isLoading)
                    CircularProgressIndicator(
                        modifier = Modifier.width(20.dp),
                        color = Color.White,
                        trackColor = MainColor,
                    )
                else
                    if (isPlaying)
                        SmallIcons(R.drawable.ic_pause, 28) {
                            player.pause()
                        }
                    else
                        SmallIcons(R.drawable.ic_play, 28) {
                            player.play()
                        }
            }
        }
    }


    DisposableEffect(Unit) {
        val playerListener = object : PlayerServiceInterface {
            override fun songBuffering(b: Boolean) {
                isLoading = b
            }

            override fun mediaItemUpdate(mediaItem: MediaItem) {
                isLoading = !mediaItem.requestMetadata.mediaUri.toString().contains("https://")
            }

            override fun playingStateChange() {
                super.playingStateChange()
                isPlaying = player.isPlaying
            }

        }
        PlayServiceListener.getInstance().addListener(playerListener)
        isPlaying = player.isPlaying

        onDispose {
            PlayServiceListener.getInstance().rmListener(playerListener)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StandByExploreItems(music: MusicData, click: () -> Unit) {
    val width = (LocalConfiguration.current.screenWidthDp / 5).dp
    val homeNav: HomeNavViewModel = hiltViewModel()

    Column(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(2))
                .size(width)
                .combinedClickable(onClick = click, onLongClick = {
                    homeNav.setSongDetailsDialog(music)
                })
        ) {
            AsyncImage(
                music.thumbnail,
                music.name,
                Modifier
                    .clip(RoundedCornerShape(2))
                    .fillMaxWidth()
            )
        }

        TextSemiBold(
            music.name ?: "",
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .width(width),
            singleLine = true,
            size = 15
        )

        Spacer(Modifier.height(7.dp))

        TextThin(
            music.artists ?: "",
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .width(width),
            singleLine = true,
            size = 13
        )

        Spacer(Modifier.height(10.dp))
    }
}


@Composable
fun SongYouMayLikeText() {
    val width = (LocalConfiguration.current.screenWidthDp / 5).dp

    Box(
        Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(2))
            .size(width)
    ) {
        Column(Modifier.align(Alignment.Center), Arrangement.Center, Alignment.CenterHorizontally) {
            TextSemiBold(
                stringResource(R.string.songs_you_may_like),
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .width(width),
                singleLine = true,
                size = 15
            )
            Spacer(Modifier.height(10.dp))
            SmallIcons(icon = R.drawable.ic_arrow_right)
        }
    }
}