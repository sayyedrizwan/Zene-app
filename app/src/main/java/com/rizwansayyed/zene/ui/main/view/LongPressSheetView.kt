package com.rizwansayyed.zene.ui.main.view

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.LikeItemType
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_ARTIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_PLAYLIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_PODCAST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.triggerHomeNav
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.utils.share.GenerateShortcuts.generateHomeScreenShortcut
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LongPressSheetView(viewModel: NavigationViewModel) {
    if (viewModel.showMediaInfoSheet != null) ModalBottomSheet(
        { viewModel.setShowMediaInfo(null) }, contentColor = MainColor, containerColor = MainColor
    ) {
        val playerViewModel: PlayerViewModel = hiltViewModel()
        val homeViewModel: HomeViewModel = hiltViewModel()
        val activity = LocalActivity.current
        var showShare by remember { mutableStateOf(false) }
        var addToPlaylistView by remember { mutableStateOf(false) }
        var showAddToHomeScreen by remember { mutableStateOf(false) }
        val mediaInfo by remember { derivedStateOf { viewModel.showMediaInfoSheet } }
        val artists by remember { derivedStateOf { viewModel.showMediaInfoSheet?.artistsList } }
        val albumInfo by remember { derivedStateOf { viewModel.showMediaInfoSheet?.albumInfo } }
        val musicPlayer by musicPlayerDB.collectAsState(null)

        mediaInfo?.let { data ->
            LazyColumn(Modifier.fillMaxWidth()) {
                stickyHeader { MediaItemView(data) }

                item {
                    if (data.type() == MusicDataTypes.SONGS || data.type() == MusicDataTypes.PODCAST_AUDIO || data.type() == MusicDataTypes.RADIO || data.type() == MusicDataTypes.VIDEOS || data.type() == MusicDataTypes.AI_MUSIC) {
                        LongPressSheetItem(R.drawable.ic_play, R.string.play) {
                            startMedia(data)
                            viewModel.setShowMediaInfo(null)
                        }

                    } else {
                        LongPressSheetItem(R.drawable.ic_arrow_up_right, R.string.view) {
                            startMedia(data)
                            viewModel.setShowMediaInfo(null)
                        }
                    }
                }

                item {
                    if (data.type() == MusicDataTypes.SONGS || data.type() == MusicDataTypes.PODCAST_AUDIO || data.type() == MusicDataTypes.RADIO || data.type() == MusicDataTypes.AI_MUSIC) {
                        LongPressSheetItem(R.drawable.ic_play_list, R.string.play_next) {
                            if (musicPlayer?.data?.id == null)
                                startMedia(viewModel.showMediaInfoSheet)
                            else
                                getPlayerS()?.playNext(viewModel.showMediaInfoSheet!!)
                            viewModel.setShowMediaInfo(null)
                        }

                        LongPressSheetItem(R.drawable.ic_add_in_queue, R.string.add_to_queue) {
                            if (musicPlayer?.data?.id == null)
                                startMedia(viewModel.showMediaInfoSheet)
                            else
                                getPlayerS()?.addToQueue(viewModel.showMediaInfoSheet!!)

                            viewModel.setShowMediaInfo(null)
                        }
                    }
                }

                if (data.type() == MusicDataTypes.SONGS && albumInfo?.id != null) {
                    item {
                        LongPressSheetItem(R.drawable.ic_vynil, R.string.go_to_album) {
                            triggerHomeNav("$NAV_PLAYLIST_PAGE${albumInfo?.id}")
                            viewModel.setShowMediaInfo(null)
                        }
                    }
                }

                if (data.type() == MusicDataTypes.SONGS && artists?.isNotEmpty() == true) item {
                    artists?.forEach {
                        LongPressSheetItem(
                            R.drawable.ic_artists,
                            txtS = "${stringResource(R.string.view)} ${it?.name}"
                        ) {
                            if ((it?.id ?: "").trim().length >= 2) {
                                triggerHomeNav("$NAV_ARTIST_PAGE${it?.id}")
                                viewModel.setShowMediaInfo(null)
                            }
                        }
                    }
                }
                else
                    if (data.type() == MusicDataTypes.PODCAST_AUDIO) {
                        if (data.secId != null) item {
                            LongPressSheetItem(
                                R.drawable.ic_podcast,
                                R.string.view_podcast_series
                            ) {
                                triggerHomeNav("$NAV_PODCAST_PAGE${data.secId}")
                                viewModel.setShowMediaInfo(null)
                            }
                        }
                        else {
                            when (val v = playerViewModel.playerPodcastInfo) {
                                ResponseResult.Empty -> {}
                                is ResponseResult.Error -> {}
                                ResponseResult.Loading -> item {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 15.dp)
                                    ) { CircularLoadingViewSmall() }
                                }

                                is ResponseResult.Success -> item {
                                    LongPressSheetItem(
                                        R.drawable.ic_podcast, R.string.view_podcast_series
                                    ) {
                                        triggerHomeNav("$NAV_PODCAST_PAGE${v.data.series?.slug}")
                                        viewModel.setShowMediaInfo(null)
                                    }
                                }
                            }
                        }
                    }

                if (data.type() == MusicDataTypes.PODCAST || data.type() == MusicDataTypes.PLAYLISTS || data.type() == MusicDataTypes.ALBUMS) item {
                    when (homeViewModel.playlistsData) {
                        ResponseResult.Empty -> {}
                        is ResponseResult.Error -> {}
                        ResponseResult.Loading -> Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 15.dp)
                        ) { CircularLoadingViewSmall() }

                        is ResponseResult.Success -> {
                            if (homeViewModel.isPlaylistAdded) LongPressSheetItem(
                                R.drawable.ic_tick, R.string.added_to_your_library
                            ) {
                                val p = PodcastPlaylistResponse(data)
                                homeViewModel.addToPlaylists(false, p, p.type())
                            } else LongPressSheetItem(
                                R.drawable.ic_layer_add, R.string.add_to_your_library
                            ) {
                                val p = PodcastPlaylistResponse(info = data)
                                homeViewModel.addToPlaylists(true, p, p.type())
                            }
                        }
                    }
                }

                if (data.type() == MusicDataTypes.SONGS || data.type() == MusicDataTypes.PODCAST_AUDIO || data.type() == MusicDataTypes.RADIO || data.type() == MusicDataTypes.VIDEOS || data.type() == MusicDataTypes.AI_MUSIC) item {
                    when (playerViewModel.isItemLiked[data.id]) {
                        LikeItemType.LOADING -> Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 15.dp)
                        ) { CircularLoadingViewSmall() }

                        LikeItemType.LIKE -> LongPressSheetItem(
                            R.drawable.ic_thumbs_up, R.string.liked
                        ) {
                            playerViewModel.likeAItem(data, false)
                        }

                        LikeItemType.NONE, null -> LongPressSheetItem(
                            R.drawable.ic_thumbs_up, R.string.like
                        ) {
                            playerViewModel.likeAItem(data, true)
                        }
                    }
                }

                if (data.type() == MusicDataTypes.SONGS || data.type() == MusicDataTypes.VIDEOS || data.type() == MusicDataTypes.RADIO || data.type() == MusicDataTypes.PODCAST_AUDIO) item {
                    LongPressSheetItem(R.drawable.ic_playlist, R.string.add_to_playlist) {
                        addToPlaylistView = true
                    }
                }

                if (data.type() != MusicDataTypes.NEWS) item {
                    LongPressSheetItem(
                        R.drawable.ic_screen_add_to_home, R.string.add_shortcut_to_home_screen
                    ) {
                        showAddToHomeScreen = true
                    }
                }

                item {
                    LongPressSheetItem(R.drawable.ic_share, R.string.share) {
                        showShare = true
                    }
                }

                item {
                    LongPressSheetItem(R.drawable.ic_cancel_close, R.string.close) {
                        viewModel.setShowMediaInfo(null)
                    }
                }

                item { Spacer(Modifier.height(55.dp)) }
            }

            if (showShare) ShareDataView(data) {
                showShare = false
            }

            if (showAddToHomeScreen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                TextAlertDialog(R.string.add_to_home_screen, R.string.add_to_home_screen_desc, {
                    showAddToHomeScreen = false
                }, {
                    generateHomeScreenShortcut(data)
                    showAddToHomeScreen = false
                })
            }


            if (addToPlaylistView) AddToPlaylistsView(data) {
                addToPlaylistView = false
            }

            LaunchedEffect(Unit) {
                activity?.let { InterstitialAdsUtils(it) }

                if (data.type() == MusicDataTypes.SONGS || data.type() == MusicDataTypes.PODCAST_AUDIO || data.type() == MusicDataTypes.RADIO || data.type() == MusicDataTypes.VIDEOS || data.type() == MusicDataTypes.AI_MUSIC) {
                    playerViewModel.likedMediaItem(data.id, data.type())
                }

                if (data.type() == MusicDataTypes.PODCAST_AUDIO && data.secId == null) {
                    playerViewModel.playerPodcastInfo(data.id!!)
                }

                if (data.type() == MusicDataTypes.PODCAST || data.type() == MusicDataTypes.PLAYLISTS || data.type() == MusicDataTypes.ALBUMS) {
                    val type =
                        if (data.type() == MusicDataTypes.PODCAST) PlaylistsType.PODCAST else PlaylistsType.PLAYLIST_ALBUMS

                    homeViewModel.isPlaylistsAdded(data.id!!, type.type)
                }

                if (data.type() == MusicDataTypes.SONGS && data.artistsList.isEmpty()) {
                    playerViewModel.similarArtistsAlbumOfSong(data.id!!, data.name, data.artists) {
                        viewModel.updateArtistsAndAlbums(it)
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediaItemView(info: ZeneMusicData) {
    Row(
        Modifier
            .background(MainColor)
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 20.dp)
            .padding(bottom = 20.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        GlideImage(
            info.thumbnail,
            info.name,
            Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .padding(horizontal = 12.dp)
                .weight(1f), Arrangement.Center, Alignment.Start
        ) {
            TextViewSemiBold(info.name ?: "", 18)
            Spacer(Modifier.height(3.dp))
            TextViewNormal(info.artists ?: "", 14, line = 4)
        }

    }
}


@Composable
fun LongPressSheetItem(img: Int, txt: Int? = null, txtS: String? = null, click: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .clickable { click() },
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        ImageIcon(img, 22)

        Box(
            Modifier
                .weight(1f)
                .padding(horizontal = 9.dp)
        ) {
            if (txt != null) TextViewNormal(stringResource(txt), 17)
            else if (txtS != null) TextViewNormal(txtS, 17)
        }
    }
}