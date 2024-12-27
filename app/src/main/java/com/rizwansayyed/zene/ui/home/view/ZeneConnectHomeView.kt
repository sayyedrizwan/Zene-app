package com.rizwansayyed.zene.ui.home.view

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.db.DataStoreManager.isZeneConnectUsedOnOtherDeviceDB
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.ui.connect.ZeneConnectActivity
import com.rizwansayyed.zene.ui.connect.view.HomeConnectVibes
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageView
import com.rizwansayyed.zene.ui.view.SmallButtonBorderText
import com.rizwansayyed.zene.ui.view.SongWaveView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.openSpecificIntent
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabled
import com.rizwansayyed.zene.utils.Utils.sendZeneConnect
import com.rizwansayyed.zene.viewmodel.PhoneVerificationViewModel
import com.rizwansayyed.zene.viewmodel.RoomDBViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun ZeneConnectHomeView() {
    val phoneViewModel: PhoneVerificationViewModel = hiltViewModel()
    val roomDB: RoomDBViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext
    val coroutines = rememberCoroutineScope()
    var contactPermission by remember { mutableStateOf(false) }
    var jobs by remember { mutableStateOf<Job?>(null) }
    val info by userInfoDB.collectAsState(initial = null)

    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            contactPermission = !it
        }

    TextTitleHeader(Pair(TextSize.BIG, R.string.zene_connect))

    if (info?.phonenumber == null) {
        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            SmallButtonBorderText(R.string.verify_phone_number_to_continue) {
                Intent(context, TrueCallerActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(this)
                }
            }
        }

        val zeneConnectEnabled by isZeneConnectUsedOnOtherDeviceDB.collectAsState(initial = false)
        if (zeneConnectEnabled) {
            TextPoppins(stringResource(R.string.connect_on_other_device), true, size = 15)
        }
    } else if (contactPermission) {
        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            SmallButtonBorderText(R.string.need_contact_permission_to_continue) {
                permission.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    } else {
        LazyRow(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            item {
                AddVibeCircle()
            }

            items(roomDB.contactsLists, key = { it.number }) {
                ZeneConnectUsers(it)
            }

            if (phoneViewModel.contactsLists.size > 0) item {
                Spacer(
                    Modifier
                        .padding(horizontal = 10.dp)
                        .size(4.dp, 35.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(MainColor)
                )
            }

            items(phoneViewModel.contactsLists) {
                ContactsUsers(it)
            }
        }

        LaunchedEffect(Unit) {
            phoneViewModel.getContactsLists()
        }

        DisposableEffect(Unit) {
            roomDB.getAllContacts()
//            jobs = coroutines.launch(Dispatchers.IO) {
//                while (true) {
                    roomDB.getAllContacts()
//                    delay(15.seconds)
//                }
//            }
            onDispose {
                jobs?.cancel()
            }
        }
    }

    LaunchedEffect(Unit) {
        contactPermission = isPermissionDisabled(Manifest.permission.READ_CONTACTS)
    }
}

@Composable
fun ZeneConnectUsers(user: ZeneConnectContactsModel) {
    var userSongs by remember { mutableStateOf<ZeneConnectContactsModel?>(null) }
    var showVibes by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(horizontal = 12.dp)
            .clickable {
                showVibes = true
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                imgBuilder(user.profilePhoto),
                user.contactName,
                Modifier
                    .padding(bottom = 4.dp)
                    .size(90.dp)
                    .clip(RoundedCornerShape(100))
            )

            if (user.currentPlayingSongID != null) Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .clickable {
                        userSongs = user
                    }) {
                AsyncImage(
                    imgBuilder(user.currentPlayingSongThumbnail),
                    user.currentPlayingSongName,
                    Modifier
                        .offset(x = 10.dp, y = (-5).dp)
                        .align(Alignment.Center)
                        .size(35.dp)
                        .clip(RoundedCornerShape(5.dp))
                )

                SongWaveView(Modifier.align(Alignment.BottomCenter))
            }

            if ((user.numberOfPosts ?: 0) > 0) Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(100))
                    .background(if (user.isNew == true) Color.Red else Color.Gray)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                TextPoppinsSemiBold(
                    "${user.numberOfPosts} ${if (user.isNew == true) "+" else ""}".trim(),
                    false, size = 17
                )
            }
        }

        Column(Modifier.widthIn(max = 200.dp), Arrangement.Center, Alignment.CenterHorizontally) {
            TextPoppins(user.contactName ?: "", false, size = 16, limit = 1)
        }
    }

    if (userSongs != null) ConnectSongListeningSheet(user) {
        userSongs = null
    }

    if (showVibes) HomeConnectVibes(user) {
        showVibes = false
    }
}


@Composable
fun ContactsUsers(contacts: ContactListData) {
    Column(
        Modifier
            .padding(horizontal = 12.dp)
            .clickable {
                sendZeneConnect(contacts)
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        ImageView(
            R.drawable.ic_user_circle,
            Modifier
                .padding(bottom = 4.dp)
                .size(90.dp)
        )

        Column(Modifier.widthIn(max = 200.dp), Arrangement.Center, Alignment.CenterHorizontally) {
            TextPoppins(contacts.name, false, size = 16, limit = 1)
            TextPoppins(contacts.number, false, size = 13, limit = 1)
            TextPoppins(stringResource(R.string.send_invite), false, size = 14, limit = 1)
        }
    }
}

@Composable
fun AddVibeCircle() {
    val context = LocalContext.current.applicationContext
    Column(
        Modifier
            .padding(horizontal = 12.dp)
            .clickable {
                Intent(context, ZeneConnectActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(this)
                }
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .padding(bottom = 4.dp)
                .border(3.dp, Color.White, RoundedCornerShape(100))
                .size(90.dp), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            ImageIcon(R.drawable.ic_dancing_people, 40)
        }
        TextPoppins(stringResource(R.string.send_vibes), false, size = 15, limit = 1)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectSongListeningSheet(user: ZeneConnectContactsModel, close: () -> Unit) {
    ModalBottomSheet(close, containerColor = MainColor) {
        Column(
            Modifier
                .padding(4.dp)
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))

            Box {
                AsyncImage(
                    imgBuilder(user.currentPlayingSongThumbnail),
                    user.currentPlayingSongName,
                    Modifier
                        .align(Alignment.Center)
                        .size(150.dp)
                        .clip(RoundedCornerShape(13.dp)),
                    contentScale = ContentScale.Crop
                )

                AsyncImage(
                    imgBuilder(user.profilePhoto),
                    user.contactName,
                    Modifier
                        .align(Alignment.BottomEnd)
                        .offset(y = 20.dp, x = 20.dp)
                        .size(50.dp)
                        .clip(RoundedCornerShape(100)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(40.dp))
            TextPoppins(stringResource(R.string.listening_too), true, size = 13)
            Spacer(Modifier.height(10.dp))
            TextPoppinsSemiBold(user.currentPlayingSongName ?: "", true, size = 15)
            Spacer(Modifier.height(10.dp))
            TextPoppins(user.currentPlayingSongArtists ?: "", true, size = 13)
            Spacer(Modifier.height(10.dp))
            SmallButtonBorderText(R.string.play_now) {
                close()
                user.currentPlayingSongID ?: return@SmallButtonBorderText
                val u = ZeneMusicDataItems(
                    user.currentPlayingSongName, user.currentPlayingSongArtists,
                    user.currentPlayingSongID, user.currentPlayingSongThumbnail, "", "SONGS"
                )
                openSpecificIntent(u, listOf(u))
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}