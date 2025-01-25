package com.rizwansayyed.zene.ui.main.connect.profile

import android.location.Geocoder
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectedUserStatus
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.view.AlertDialogWithImage
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun ConnectProfileDetailsView(data: ConnectUserInfoResponse, viewModel: ConnectViewModel) {
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
                if (data.status?.isConnected() == ConnectedUserStatus.FRIENDS) {
                    Row(
                        Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically
                    ) {
                        ImageWithBorder(R.drawable.ic_message_multiple) {

                        }

                        ImageWithBorder(R.drawable.ic_music_note) {

                        }

                        ImageWithBorder(R.drawable.ic_location) {

                        }
                    }
                    Spacer(Modifier.height(50.dp))
                }
            }
        }
        item {
            if (data.songDetails?.id != null && data.songDetails.name != null) {
                SongListeningTo(data.songDetails)
                Spacer(Modifier.height(50.dp))
            }
        }
        item {
            if (data.user?.isUserLocation() == true) {
                ConnectUserMapView(data.user)
                Spacer(Modifier.height(50.dp))
            }
        }
        item {
            if (data.topSongs?.isNotEmpty() == true) {
                ConnectTopListenedView(data.topSongs)
                Spacer(Modifier.height(60.dp))
            }
        }
        item {
            if (data.status?.isConnected() == ConnectedUserStatus.FRIENDS) {
                ConnectSettingsView(data, viewModel)
                Spacer(Modifier.height(150.dp))
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongListeningTo(song: ZeneMusicData) {
    var showPlaySong by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .clickable { showPlaySong = true },
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

    if (showPlaySong) AlertDialogWithImage(song.thumbnail, song.name, {
        showPlaySong = false
    }, {
        showPlaySong = false
    })
}

@Composable
fun TopSheetView(data: ConnectUserInfoResponse, viewModel: ConnectViewModel) {
    val context = LocalContext.current.applicationContext
    var areaName by remember { mutableStateOf("") }
    var showRequestSentAlert by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            TextViewSemiBold(data.user?.name ?: "", 25)
            TextViewNormal("@${data.user?.username}", 14)

            if (data.user?.isUserLocation() == true) TextViewNormal(areaName, 14)
            else TextViewNormal(data.user?.country ?: "", 14)
        }
        when (data.status?.isConnected()) {
            ConnectedUserStatus.FRIENDS -> ButtonWithBorder(R.string.friends) {

            }

            ConnectedUserStatus.REQUESTED -> {
                if (data.didRequestToYou == true) {
                    Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
                        ButtonWithBorder(R.string.accept) {
                            viewModel.acceptConnectRequest(data.user?.email)
                        }
                        Row(Modifier.clickable {
                            data.user?.email?.let { viewModel.doRemove(it, true) }
                        }) {
                            ImageIcon(R.drawable.ic_delete, 20)
                        }
                    }
                } else ButtonWithBorder(R.string.sent) {
                    showRequestSentAlert = true
                }

            }

            ConnectedUserStatus.NONE, null -> ButtonWithBorder(R.string.add) {
                viewModel.updateAddStatus(data, false)
            }
        }
    }

    if (showRequestSentAlert) TextAlertDialog(
        R.string.cancel_request,
        R.string.are_you_sure_want_to_cancel_add_request,
        { showRequestSentAlert = false }, {
            showRequestSentAlert = false
            viewModel.updateAddStatus(data, true)
        }
    )

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
