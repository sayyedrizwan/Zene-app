package com.rizwansayyed.zene.ui.main.connect.connectview

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast

@Composable
fun ConnectRecentContactsView() {
    val needContact = stringResource(R.string.need_location_permission_to_read_contact)
    var contactsView by remember { mutableStateOf(false) }
    var editUserView by remember { mutableStateOf(false) }
    val contactPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) contactsView = true
            else {
                needContact.toast()
                openAppSettings()
            }
        }

    Spacer(Modifier.height(52.dp))
    Row(Modifier.padding(horizontal = 9.dp)) {
        TextViewBold(stringResource(R.string.friends), 18)
        Spacer(Modifier.weight(1f))
        Box(Modifier.clickable { editUserView = true }) {
            ImageIcon(R.drawable.ic_user_edit, 23)
        }
        Spacer(Modifier.width(14.dp))
        Box(Modifier.clickable { contactPermission.launch(Manifest.permission.READ_CONTACTS) }) {
            ImageIcon(R.drawable.ic_user_search, 23)
        }
        Spacer(Modifier.width(14.dp))
        Box(Modifier.clickable { editUserView = true }) {
            ImageIcon(R.drawable.ic_bookmark, 23)
        }
    }
    Spacer(Modifier.height(12.dp))

    if (contactsView) Dialog(
        { contactsView = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ContactListsInfo()
    }
    if (editUserView) Dialog(
        { editUserView = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ConnectEditProfileView()
    }
}