package com.rizwansayyed.zene.ui.connect.view

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.viewmodel.RoomDBViewModel


@Composable
fun SendButtonView(modifier: Modifier = Modifier) {
    var showSendAlert by remember { mutableStateOf(false) }
    val sendStatusContact = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier
            .padding(bottom = 55.dp, end = 20.dp)
            .clickable {
                showSendAlert = true
            }
            .size(50.dp)
            .shadow(12.dp, RoundedCornerShape(100))
            .background(MainColor),
        Arrangement.Center,
        Alignment.CenterHorizontally) {
        ImageIcon(R.drawable.ic_sent, size = 35)
    }

    if (showSendAlert) ZeneConnectVibesSendView(sendStatusContact) {
        showSendAlert = false
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZeneConnectVibesSendView(
    sendStatusContact: SnapshotStateMap<String, Boolean>, click: () -> Unit
) {
    val roomDB: RoomDBViewModel = hiltViewModel()

    ModalBottomSheet(click, containerColor = MainColor, contentColor = MainColor) {
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(10.dp))
                TextPoppins(stringResource(R.string.once_send_vibes_cannot_undo), true, size = 15)
                Spacer(Modifier.height(10.dp))
            }

            item {
                if (roomDB.contactsLists.isEmpty()) {
                    TextPoppins(
                        stringResource(R.string.no_users_to_vibe), true, size = 15
                    )
                } else {
                    TextPoppinsSemiBold(stringResource(R.string.contacts), false, size = 15)
                    Spacer(Modifier.height(5.dp))
                }
            }
            items(roomDB.contactsLists) {
                SendUserInfo(it, sendStatusContact)
            }

            item {
                Spacer(Modifier.height(150.dp))
            }
        }


        LaunchedEffect(Unit) {
            roomDB.getAllContacts()
        }
    }
}

@Composable
fun SendUserInfo(
    contacts: ZeneConnectContactsModel, sendStatusContact: SnapshotStateMap<String, Boolean>
) {
    Row(
        Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        AsyncImage(
            imgBuilder(contacts.profilePhoto),
            contacts.contactName,
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 9.dp)
        ) {
            TextPoppins(v = contacts.contactName ?: "", false, size = 16)
            Spacer(Modifier.height(4.dp))
            TextPoppins(v = "Listening to Sanam teri Kasam", false, size = 13)
        }

        Box(Modifier.clickable {
            sendStatusContact[contacts.number] = true
        }) {
            ImageIcon(
                if (sendStatusContact[contacts.number] == true) R.drawable.ic_tick else R.drawable.ic_sent,
                24
            )
        }
    }
}