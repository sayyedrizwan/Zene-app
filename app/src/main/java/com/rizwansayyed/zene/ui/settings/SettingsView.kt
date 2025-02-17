package com.rizwansayyed.zene.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.settings.view.SettingsPersonalInfo
import com.rizwansayyed.zene.ui.settings.view.SettingsProfilePhotoView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_GO_BACK

@Composable
fun SettingsView() {
    val userInfo by DataStorageManager.userInfo.collectAsState(null)
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Spacer(Modifier.height(65.dp))
            Box(Modifier
                .rotate(180f)
                .padding(horizontal = 5.dp)
                .clickable { NavigationUtils.triggerHomeNav(NAV_GO_BACK) }) {
                ImageIcon(R.drawable.ic_arrow_right, 27)
            }
        }
        item {
            SettingsProfilePhotoView(userInfo)
        }

        item {
            SettingsPersonalInfo(userInfo)
        }

        item { Spacer(Modifier.height(300.dp)) }
    }

    BackHandler {
        NavigationUtils.triggerHomeNav(NAV_GO_BACK)
    }
}