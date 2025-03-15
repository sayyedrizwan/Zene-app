package com.rizwansayyed.zene.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.settings.view.SettingsPersonalInfo
import com.rizwansayyed.zene.ui.settings.view.SettingsPlaybackView
import com.rizwansayyed.zene.ui.settings.view.SettingsProfilePhotoView
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_GO_BACK

@Composable
fun SettingsView() {
    val userInfo by DataStorageManager.userInfo.collectAsState(null)
    Box(Modifier.fillMaxWidth()) {
        LazyColumn(Modifier.fillMaxSize()) {
            item { Spacer(Modifier.height(100.dp)) }
            item { SettingsProfilePhotoView(userInfo) }

            item { SettingsPersonalInfo(userInfo) }

            item { Spacer(Modifier.height(50.dp)) }

            item { SettingsPlaybackView() }

            item { Spacer(Modifier.height(300.dp)) }
        }

        ButtonArrowBack()
    }

    BackHandler {
        NavigationUtils.triggerHomeNav(NAV_GO_BACK)
    }
}