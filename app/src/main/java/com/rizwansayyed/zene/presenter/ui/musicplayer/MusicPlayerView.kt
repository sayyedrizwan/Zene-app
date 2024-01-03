package com.rizwansayyed.zene.presenter.ui.musicplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.GlobalNativeFullAds
import com.rizwansayyed.zene.presenter.ui.backgroundPalette
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.LiveBroadcastText
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicActionButtons
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlayerArtists
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlayerArtistsMerchandise
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlayerButtons
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlayerImages
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlayerLyrics
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlayerRelatedSongs
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlayerSliders
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.RadioActionButtons
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.SongsThumbnailsWithList
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.TopPlayerHeader
import com.rizwansayyed.zene.service.player.listener.PlayServiceListener
import com.rizwansayyed.zene.service.player.listener.PlayerServiceInterface
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.littleVibrate
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun MusicPlayerView(player: ExoPlayer, showedOnLockScreen: Boolean) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val p by musicPlayerData.collectAsState(initial = runBlocking(Dispatchers.IO) { musicPlayerData.first() })

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .backgroundPalette()
            .verticalScroll(rememberScrollState())
    ) {
        TopPlayerHeader(showedOnLockScreen, p)

        SongsThumbnailsWithList(p)


        if (p?.playType == MusicType.RADIO)
            LiveBroadcastText()
        else
            MusicPlayerSliders(player)

        if (p?.playType == MusicType.RADIO) {
            RadioActionButtons(player)

            Spacer(Modifier.height(20.dp))

            GlobalNativeFullAds()
        } else {
            MusicPlayerButtons(player)

            Spacer(Modifier.height(20.dp))

            MusicActionButtons(p)

            Spacer(Modifier.height(30.dp))

            MusicPlayerLyrics(playerViewModel, player)

            GlobalNativeFullAds()

            MusicPlayerArtistsMerchandise(playerViewModel)

            MusicPlayerArtists(playerViewModel)

            MusicPlayerImages(playerViewModel)

            GlobalNativeFullAds()

            MusicPlayerRelatedSongs(playerViewModel)

            GlobalNativeFullAds()
        }

        Spacer(Modifier.height(90.dp))
    }

    LaunchedEffect(p) {
        if (p?.playType == MusicType.RADIO) return@LaunchedEffect

        if (p?.songID != player.currentMediaItem?.mediaId) p?.let { playerViewModel.init(it) }

        if (p?.songID != player.currentMediaItem?.mediaId || playerViewModel.relatedSongs == DataResponse.Empty)
            p?.let { playerViewModel.similarSongsArtists(it.v?.songID ?: "") }

        if (p?.songID != player.currentMediaItem?.mediaId || playerViewModel.videoSongs == DataResponse.Empty) {
            playerViewModel.setMusicType(Utils.MusicViewType.MUSIC)
            p?.let { playerViewModel.searchLyricsAndSongVideo(it.v?.songName, it.v?.artists) }
        }

        p?.songID?.let { playerViewModel.offlineSongDetails(it) }
        p?.let { playerViewModel.searchLyrics(it) }

        registerEvent(FirebaseEvents.FirebaseEvent.SONG_CHANGE_FROM_MUSIC_PLAYER)
    }

    DisposableEffect(Unit) {
        registerEvent(FirebaseEvents.FirebaseEvent.OPEN_MUSIC_PLAYER)
        littleVibrate()
        onDispose {
            littleVibrate()
        }
    }
}


@Composable
fun BottomNavImage(player: ExoPlayer) {
    val p by musicPlayerData.collectAsState(initial = null)
    val coroutine = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    if (p?.v?.thumbnail != null)
        Row(Modifier.fillMaxWidth()) {
            Spacer(Modifier.weight(1f))
            Box(
                Modifier
                    .background(Color.Black)
                    .clickable {
                        coroutine.launch(Dispatchers.IO) {
                            val playerData = musicPlayerData
                                .first()
                                ?.apply {
                                    show = true
                                }
                            musicPlayerData = flowOf(playerData)
                        }
                    }) {

                AsyncImage(p?.v?.thumbnail, p?.v?.songName, Modifier.size(50.dp))


                if (isLoading)
                    SmallLoadingSpinner(Modifier.align(Alignment.BottomCenter))
                else
                    Image(
                        painterResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                        "",
                        Modifier
                            .align(Alignment.Center)
                            .size(20.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
            }
            Spacer(Modifier.width(7.dp))
        }

    DisposableEffect(Unit) {
        val playerListener = object : PlayerServiceInterface {
            override fun songInfoDownloading(b: Boolean) {
                isLoading = b
            }

            override fun songBuffering(b: Boolean) {
                isLoading = b
            }

            override fun mediaItemUpdate(mediaItem: MediaItem) {
                if (!mediaItem.requestMetadata.mediaUri.toString().contains("https://"))
                    isLoading = true
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


@Composable
fun SmallLoadingSpinner(modifier: Modifier, size: Int = 50) {
    Column(modifier.size(size.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(12.dp))

        CircularProgressIndicator(
            modifier = Modifier.width(20.dp),
            color = Color.White,
            trackColor = MainColor,
        )
    }
}