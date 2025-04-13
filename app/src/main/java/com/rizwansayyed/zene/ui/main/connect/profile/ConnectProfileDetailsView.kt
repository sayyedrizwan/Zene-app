package com.rizwansayyed.zene.ui.main.connect.profile

import android.Manifest
import android.location.Geocoder
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserWall
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.FRIENDS
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.ME
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.NONE
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.REQUESTED
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibeItemView
import com.rizwansayyed.zene.ui.main.connect.connectchat.ConnectProfileMessagingView
import com.rizwansayyed.zene.ui.main.home.view.SavedPlaylistsPodcastView
import com.rizwansayyed.zene.ui.main.home.view.TextSimpleCards
import com.rizwansayyed.zene.ui.main.home.view.TextSimpleCardsImg
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.BioAuthMetric
import com.rizwansayyed.zene.utils.ChatTempDataUtils.doOpenChatOnConnect
import com.rizwansayyed.zene.utils.SnackBarManager
import com.rizwansayyed.zene.utils.share.ShareContentUtils.shareConnectURL
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectProfileDetailsView(data: ConnectUserInfoResponse, viewModel: ConnectViewModel) {
    val coroutine = rememberCoroutineScope()
    val context = LocalActivity.current
    val bioAuthMetric = BioAuthMetric(R.string.authenticate_to_view_chat, context)

    var showSendMessage by remember { mutableStateOf(false) }
    var sendLocation by remember { mutableStateOf(false) }
    var showPosts by remember { mutableStateOf(ConnectUserWall.STATUS) }
    var showPartyDialog by remember { mutableStateOf(false) }
    var createPlaylistsDialog by remember { mutableStateOf(false) }
    var playlistDialog by remember { mutableStateOf(false) }
    var playlistPage by remember { mutableIntStateOf(0) }

    val locationPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                sendLocation = true
            }
        }

    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
    ) {
        item {
            if (data.user?.email == null && data.user?.username == null) {
                TextViewSemiBold(stringResource(R.string.no_user_found), 25, center = true)
            }
            TopSheetView(data, viewModel)
            Spacer(Modifier.height(30.dp))
        }

        item {
            if (data.user?.email != null && data.user.username != null) {
                if (data.isConnected() == FRIENDS) {
                    Row(
                        Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically
                    ) {
                        Box {
                            ImageWithBorder(R.drawable.ic_message_multiple) {
                                coroutine.launch {
                                    val lock = DataStorageManager.lockChatSettings(data.user.email)
                                        .firstOrNull()
                                    if (lock == true) {
                                        bioAuthMetric.checkAuth { auth ->
                                            if (auth) showSendMessage = true
                                        }
                                    } else showSendMessage = true
                                }
                            }

                            if ((data.unReadMessages ?: 0) > 0) Box(
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(y = (-7).dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.Red)
                                    .padding(horizontal = 9.dp)
                            ) {
                                if ((data.unReadMessages ?: 0) >= 9) TextViewBold("9+", 10)
                                else TextViewBold(data.unReadMessages.toString(), 10)
                            }
                        }


                        ImageWithBorder(R.drawable.ic_party) {
                            showPartyDialog = true
                        }

                        ImageWithBorder(R.drawable.ic_location) {
                            locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    }
                    Spacer(Modifier.height(50.dp))
                }
            }
        }

        if (data.isConnected() == FRIENDS || data.isConnected() == ME) {
            item {
                Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
                    TextSimpleCards(
                        showPosts == ConnectUserWall.STATUS, stringResource(R.string.vibes_status)
                    ) {
                        showPosts = ConnectUserWall.STATUS
                    }

                    if (data.isConnected() == FRIENDS) {
                        TextSimpleCards(
                            showPosts == ConnectUserWall.PLAYLISTS,
                            stringResource(R.string.playlists)
                        ) {
                            showPosts = ConnectUserWall.PLAYLISTS
                        }

                        AnimatedVisibility(showPosts == ConnectUserWall.PLAYLISTS) {
                            TextSimpleCardsImg(R.drawable.ic_plus_sign) {
                                playlistDialog = true
                            }
                        }
                    }
                    TextSimpleCards(
                        showPosts == ConnectUserWall.INFO, stringResource(R.string.info)
                    ) {
                        showPosts = ConnectUserWall.INFO
                    }
                }
            }
        }

        item { Spacer(Modifier.height(20.dp)) }

        if (data.isConnected() == FRIENDS || data.isConnected() == ME) {
            if (showPosts == ConnectUserWall.STATUS) {
                if (data.vibes?.isEmpty() == true) item {
                    TextViewBold(stringResource(R.string.no_posts), 19, center = true)
                }

                items(data.vibes ?: emptyList()) {
                    ConnectVibeItemView(it)
                }
            } else if (showPosts == ConnectUserWall.PLAYLISTS && data.isConnected() == FRIENDS) {
                item {
                    if (viewModel.isLoadingConnectPlaylist) CircularLoadingView()

                    if (viewModel.connectPlaylistsLists.isEmpty() && !viewModel.isLoadingConnectPlaylist)
                        TextViewBold(stringResource(R.string.no_playlists), 19, center = true)


                    LazyRow(Modifier.fillMaxWidth()) {
                        items(viewModel.connectPlaylistsLists) {
                            SavedPlaylistsPodcastView(it, Modifier.width(175.dp))
                        }
                    }
                }
            } else {
                if (data.isConnected() != ME) item { UsersSettingsOfView(data) }

                item {
                    if (data.songDetails?.id != null && data.songDetails.name != null) {
                        SongListeningTo(data.songDetails)
                        Spacer(Modifier.height(50.dp))
                    }
                }
                if (data.otherStatus?.locationSharing == true || data.isConnected() == ME) item {
                    ConnectUserMapView(data.user)
                    Spacer(Modifier.height(50.dp))
                }

                if (data.otherStatus?.lastListeningSong == true || data.isConnected() == ME) item {
                    if (data.topSongs?.isNotEmpty() == true) {
                        ConnectTopListenedView(data.topSongs)
                    } else {
                        Spacer(Modifier.height(20.dp))
                        TextViewBold(
                            stringResource(R.string.no_listened_song_found_of_user), center = true
                        )
                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }

        item { Spacer(Modifier.height(60.dp)) }

        item {
            if (data.isConnected() == FRIENDS) {
                ConnectSettingsView(data, viewModel)
                Spacer(Modifier.height(150.dp))
            }
        }

        if (data.isConnected() == ME) item { Spacer(Modifier.height(100.dp)) }
        item { Spacer(Modifier.height(150.dp)) }
    }

    if (showSendMessage) ConnectProfileMessagingView(data, viewModel) {
        showSendMessage = false
        data.user?.email?.let { email ->
            viewModel.connectUserInfoEmpty()
            viewModel.connectUserInfo(email)
        }
    }

    LaunchedEffect(Unit) {
        if (doOpenChatOnConnect) {
            coroutine.launch {
                val lock = DataStorageManager.lockChatSettings(data.user?.email ?: "").firstOrNull()
                if (lock == true) {
                    bioAuthMetric.checkAuth { auth ->
                        if (auth) showSendMessage = true
                    }
                } else showSendMessage = true
            }
            doOpenChatOnConnect = false
        }

        if (data.isConnected() == FRIENDS)
            viewModel.connectPlaylists(data.user?.email, playlistPage)

    }

    if (sendLocation) ModalBottomSheet(
        { sendLocation = false }, contentColor = MainColor, containerColor = MainColor
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            ConnectLocationButton(data, viewModel) {
                sendLocation = false
            }
        }
    }

    if (showPartyDialog) DialogPartyInfo(data) {
        showPartyDialog = false
    }

    if (playlistDialog) DialogPlaylistSyncInfo {
        playlistDialog = false
        if (it) createPlaylistsDialog = true
    }


    if (createPlaylistsDialog) DialogConnectUserAddPlaylist(data.user?.email) {
        playlistPage = 0
        viewModel.connectPlaylists(data.user?.email, 0)
        createPlaylistsDialog = false
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongListeningTo(song: ZeneMusicData) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { NavigationUtils.triggerInfoSheet(song) },
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        GlideImage(
            song.thumbnail, song.name, Modifier.size(120.dp), contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .padding(horizontal = 5.dp)
                .weight(1f)
        ) {
            TextViewBold(stringResource(R.string.listening_to), 13)
            TextViewNormal(song.name ?: "", 18, line = 2)
            TextViewLight(song.artists ?: "", 13, line = 3)
        }
        GlideImage(
            R.raw.wave_animiation, "wave", Modifier.size(70.dp), contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun UsersSettingsOfView(data: ConnectUserInfoResponse) {
    val musicSharing = stringResource(R.string.top_songs_sharing_off_by_user)
    val locationSharing = stringResource(R.string.location_sharing_off_by_user)
    val notificationSilent = stringResource(R.string.notification_from_you_silent_by_user)

    if (data.otherStatus?.lastListeningSong == false || data.otherStatus?.locationSharing == false || data.otherStatus?.silentNotification == true) {
        Spacer(Modifier.height(20.dp))
        Row(
            Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
        ) {
            if (data.otherStatus.lastListeningSong == false) ImageWithBorder(
                R.drawable.ic_music_note, Color.Red
            ) {
                SnackBarManager.showMessage(musicSharing)
            }

            Spacer(Modifier.width(20.dp))
            if (data.otherStatus.locationSharing == false) ImageWithBorder(
                R.drawable.ic_location, Color.Red
            ) {
                SnackBarManager.showMessage(locationSharing)
            }

            Spacer(Modifier.width(20.dp))
            if (data.otherStatus.silentNotification == true) ImageWithBorder(
                R.drawable.ic_notification_off, Color.Red
            ) {
                SnackBarManager.showMessage(notificationSilent)
            }
        }
        Spacer(Modifier.height(30.dp))
    }
}

@Composable
fun TopSheetView(data: ConnectUserInfoResponse, viewModel: ConnectViewModel) {
    val context = LocalContext.current.applicationContext

    var areaName by remember { mutableStateOf("") }
    var showRequestSentAlert by remember { mutableStateOf(false) }
    var unFriendAlert by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        if (data.user?.email != null && data.user.username != null) {
            Column(
                Modifier
                    .weight(1f)
                    .padding(bottom = 5.dp)
            ) {
                TextViewSemiBold(data.user.name ?: "", 25)
                TextViewNormal("@${data.user.username}", 14)

                if (data.user.isUserLocation()) TextViewNormal(areaName, 14)
                else TextViewNormal(data.user.country ?: "", 14)

                if (data.user.connect_status?.trim()?.isNotEmpty() == true) {
                    Spacer(Modifier.height(5.dp))
                    TextViewBold("\uD83E\uDD14 \uD83D\uDC49  '${data.user.connect_status}'", 20)
                    Spacer(Modifier.height(10.dp))
                }
            }

            when (data.isConnected()) {
                FRIENDS -> ButtonWithBorder(R.string.friends) {
                    unFriendAlert = true
                }

                REQUESTED -> {
                    if (data.didRequestToYou == true) {
                        Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
                            ButtonWithBorder(R.string.accept) {
                                viewModel.acceptConnectRequest(data.user.email)
                            }

                            Row(Modifier.clickable {
                                data.user.email.let { viewModel.doRemove(it, true) }
                            }) {
                                ImageIcon(R.drawable.ic_delete, 20)
                            }
                        }
                    } else ButtonWithBorder(R.string.sent) {
                        showRequestSentAlert = true
                    }
                }

                NONE -> ButtonWithBorder(R.string.add) {
                    viewModel.updateAddStatus(data, false)
                }

                ME -> {
                    ButtonWithBorder(R.string.share) {
                        shareConnectURL()
                    }
                }
            }
        }
    }

    if (showRequestSentAlert) TextAlertDialog(
        R.string.cancel_request,
        R.string.are_you_sure_want_to_cancel_add_request,
        { showRequestSentAlert = false },
        {
            showRequestSentAlert = false
            data.user?.email?.let { viewModel.doRemove(it, true) }
        })

    if (unFriendAlert) TextAlertDialog(
        R.string.remove_as_friend,
        R.string.remove_as_friend_desc,
        { unFriendAlert = false },
        {
            unFriendAlert = false
            viewModel.updateAddStatus(data, true)
        })

    LaunchedEffect(Unit) {
        areaName = withContext(Dispatchers.IO) {
            try {
                val lat = data.user?.location?.substringBefore(",")?.trim()?.toDouble() ?: 0.0
                val lon = data.user?.location?.substringAfter(",")?.trim()?.toDouble() ?: 0.0

                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                if (!addresses.isNullOrEmpty()) {
                    "${addresses[0].subLocality}, ${addresses[0].locality}"
                } else {
                    data.user?.country ?: ""
                }
            } catch (e: Exception) {
                data.user?.country ?: ""
            }
        }
    }
}
