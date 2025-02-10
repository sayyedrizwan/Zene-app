package com.rizwansayyed.zene.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.settings.view.SettingsPersonalInfo
import com.rizwansayyed.zene.ui.settings.view.SettingsProfilePhotoView

@Composable
fun SettingsView() {
    val userInfo by DataStorageManager.userInfo.collectAsState(null)
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            SettingsProfilePhotoView(userInfo)
        }

        item {
            SettingsPersonalInfo(userInfo)
        }
    }
}