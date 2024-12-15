package com.rizwansayyed.zene.ui.zeneconnect

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.isZeneConnectUsedOnOtherDeviceDB
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.ui.connect.ZeneConnectActivity
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.home.view.TextTitleHeader
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageView
import com.rizwansayyed.zene.ui.view.SmallButtonBorderText
import com.rizwansayyed.zene.ui.view.SongWaveView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabled
import com.rizwansayyed.zene.utils.Utils.sendZeneConnect
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.PhoneVerificationViewModel
import com.rizwansayyed.zene.viewmodel.RoomDBViewModel

@Composable
fun ZeneConnectHomeView() {
    val phoneViewModel: PhoneVerificationViewModel = hiltViewModel()
    val roomDB: RoomDBViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext
    var contactPermission by remember { mutableStateOf(false) }
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
            roomDB.getAllContacts()
            phoneViewModel.getContactsLists()
        }
    }

    LaunchedEffect(Unit) {
        contactPermission = isPermissionDisabled(Manifest.permission.READ_CONTACTS)
    }
}

@Composable
fun ZeneConnectUsers(user: ZeneConnectContactsModel) {
    Column(Modifier.padding(horizontal = 12.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Box {
            AsyncImage(
                imgBuilder(user.profilePhoto),
                user.contactName,
                Modifier
                    .padding(bottom = 4.dp)
                    .size(90.dp)
                    .clip(RoundedCornerShape(100))
            )

            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .clickable {
                        "play_song".toast()
                    }) {
                AsyncImage(
                    imgBuilder("https://lh3.googleusercontent.com/_YrGlG1r1LWNgS78199Cv2R8HZEl_o4l7DeyTaTPGkpGQHcaBI9UCMwoecf2HyEWbAyPqsqcf_L9mSw=w544-h544-l90-rj"),
                    user.contactName,
                    Modifier
                        .offset(x = 10.dp, y = (-5).dp)
                        .align(Alignment.Center)
                        .size(35.dp)
                        .clip(RoundedCornerShape(5.dp))
                )

                SongWaveView(Modifier.align(Alignment.BottomCenter))
            }

            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Red)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                TextPoppinsSemiBold("4", false, size = 17)
            }
        }

        Column(Modifier.widthIn(max = 200.dp), Arrangement.Center, Alignment.CenterHorizontally) {
            TextPoppins(user.contactName ?: "", false, size = 16, limit = 1)
        }
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