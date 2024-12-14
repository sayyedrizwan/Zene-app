package com.rizwansayyed.zene.ui.zeneconnect

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.home.view.TextTitleHeader
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerActivity
import com.rizwansayyed.zene.ui.view.SmallButtonBorderText
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabled
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
    } else if (contactPermission) {
        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            SmallButtonBorderText(R.string.need_contact_permission_to_continue) {
                permission.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    } else {
        LazyRow(Modifier.fillMaxWidth()) {
            items(roomDB.contactsLists) {
                Column(Modifier, Arrangement.Center, Alignment.CenterHorizontally) {
                    AsyncImage(
                        imgBuilder(it.profilePhoto),
                        it.contactName,
                        Modifier
                            .size(65.dp)
                            .clip(RoundedCornerShape(100))
                    )
                }
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