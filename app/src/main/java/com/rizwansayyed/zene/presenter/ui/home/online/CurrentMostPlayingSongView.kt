package com.rizwansayyed.zene.presenter.ui.home.online


import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.toMusicDataList
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBoldBig
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.presenter.util.UiUtils.convertMoney
import com.rizwansayyed.zene.utils.Utils.startPlayingSong
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalFoundationApi::class)
@androidx.media3.common.util.UnstableApi
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
                    itemsIndexed(v.item ?: emptyList()) { i, m ->
                        MostPlayedSongView(
                            m,
                            Modifier
                                .animateItemPlacement()
                                .clickable {
                                    val l = v.item?.toMusicDataList()
                                    startPlayingSong(l?.toTypedArray(), i)
                                    CoroutineScope(Dispatchers.Main).launch {
//                                        val metadata = MediaMetadata
//                                            .Builder()
//                                            .setTitle(it.name)
//                                            .setAlbumTitle("Albums")
//                                            .setDisplayTitle(it.name)
//                                            .setArtist(it.artists)
//                                            .setArtworkUri(it.thumbnail?.toUri())
//                                            .build()
//
//
//                                        val mediaItem = MediaItem
//                                            .Builder()
//                                            .setUri("https://www.pagalwrold.com/siteuploads/files/sfd34/16973/Tera%20Mera%20Safar%20(Slowed%20Reverb)_320(PagalWorld).mp3")
//                                            .setMediaId(
//                                                it.pId ?: UUID
//                                                    .randomUUID()
//                                                    .toString()
//                                            )
//                                            .setMediaMetadata(metadata)
//                                            .build()
//
//                                        player.apply {
//                                            playWhenReady = true
//                                            setMediaItem(mediaItem)
//                                            prepare()
//                                            play()
//                                        }
//
//                                        val defaultMediaNotificationProvider =
//                                            DefaultMediaNotificationProvider(mContext)
//                                        defaultMediaNotificationProvider.setSmallIcon(R.drawable.blb_reverse_icon)
//                                        val mediaNotification =
//                                            defaultMediaNotificationProvider
//                                                .createNotification(
//                                                    mediaSession,
//                                                    customLayout,
//                                                    actionFactory,
//                                                    onNotificationChangedCallback
//                                                )
//
//
//                                        val notificationManager =
//                                            PlayerNotificationManager
//                                                .Builder(context, 33, "sss")
//                                                .setChannelImportance(NotificationUtil.IMPORTANCE_DEFAULT)
//                                                .setSmallIconResourceId(R.drawable.ic_play)
//                                                .setChannelDescriptionResourceId(R.string.app_name)
//                                                .setChannelNameResourceId(R.string.app_name)
////                                                .setMediaDescriptionAdapter(audioDescriptor)
////                                                .setNotificationListener(notificationListener)
//                                                .build()
//
//
//                                        notificationManager.setPriority(NotificationCompat.PRIORITY_MIN)
//                                        notificationManager.setUseRewindAction(false)
//                                        notificationManager.setUseFastForwardAction(false)
//                                        notificationManager.setUsePreviousAction(true)
//                                        notificationManager.setUsePlayPauseActions(true)
//                                        notificationManager.setUseNextAction(true)
//                                        notificationManager.setPlayer(player)
//
//
//                                        val metadata = getMetaDataFromMediaClass(media)
//                                        val mediaItem = MediaItem.Builder()
//                                            .setUri(media.dataURL)
//                                            .setMediaId(media.mediaID)
//                                            .setMediaMetadata(metadata)
//                                            .build()
//
//                                        player.apply {
//                                            setMediaItem(mediaItem)
//                                            prepare()
//                                            play()
//                                        }
//
//                                        "running".toast()
//                                        val mediaItem = MediaItem
//                                            .Builder()
//                                            .setMediaId(it.pId!!)
//                                            .setUri(it.pId)
//                                            .setMediaMetadata(
//                                                MediaMetadata
//                                                    .Builder()
//                                                    .setTitle(it?.name)
//                                                    .setArtist(it?.artists)
//                                                    .setArtworkUri(it.thumbnail?.toUri())
//                                                    .build()
//                                            )
//                                            .build()
//                                        player.setMediaItem(mediaItem, true)
//                                        player.playWhenReady = true
//                                        player.prepare()
//                                        player.play()
//
//                                        val mediaItem2 = MediaItem
//                                            .Builder()
//                                            .setMediaId(it.pId!!)
//                                            .setUri("https://www.pagalwrold.com/siteuploads/files/sfd34/16973/Tera%20Mera%20Safar%20(Slowed%20Reverb)_320(PagalWorld).mp3")
//                                            .setMediaMetadata(
//                                                MediaMetadata
//                                                    .Builder()
//                                                    .setTitle(it?.name)
//                                                    .setArtist(it?.artists)
//                                                    .setArtworkUri(it.thumbnail?.toUri())
//                                                    .build()
//                                            )
//                                            .build()
////                                        player.setMediaItem(mediaItem2, true)
//
//
//                                        player.setAudioAttributes(
//                                            AudioAttributes
//                                                .Builder()
//                                                .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
//                                                .setUsage(USAGE_MEDIA)
//                                                .build(), true
//                                        )
//
//                                        val call = object : MediaSession.Callback {
//
//                                        }
//
//                                        val mediaSession =
//                                            MediaSession
//                                                .Builder(context, player)
//                                                .setCallback(call)
//                                                .build()
//
//                                        mediaSession.  setMediaNotificationProvider(object :
//                                            MediaNotification.Provider {
//                                            override fun createNotification(
//                                                mediaSession: MediaSession,
//                                                customLayout: ImmutableList<CommandButton>,
//                                                actionFactory: MediaNotification.ActionFactory,
//                                                onNotificationChangedCallback: MediaNotification.Provider.Callback
//                                            ): MediaNotification {
//                                                // This run every time when I press buttons on notification bar:
//                                                return updateNotification(mediaSession)
//                                            }
//
//                                            override fun handleCustomCommand(
//                                                session: MediaSession,
//                                                action: String,
//                                                extras: Bundle
//                                            ): Boolean {
//                                                return false
//                                            }
//                                        })
//
//
//                                        delay(3.seconds)
//                                        player.removeMediaItem(player.currentMediaItemIndex)
//                                        player.addMediaItem(
//                                            player.currentMediaItemIndex,
//                                            mediaItem2
//                                        )
//                                        player.playWhenReady = true
//                                        player.prepare()
//                                        player.play()

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