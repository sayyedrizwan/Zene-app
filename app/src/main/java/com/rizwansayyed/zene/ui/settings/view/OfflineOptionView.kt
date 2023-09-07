package com.rizwansayyed.zene.ui.settings.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.datastore.DataStoreMusicEnum
import com.rizwansayyed.zene.ui.settings.ViewLocalSongs
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.isPermissionGranted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

@Composable
fun OfflineOptionSettings() {

    val context = LocalContext.current.applicationContext

    val offlineMusics by dataStoreManager.offlineMusics.collectAsState(runBlocking { dataStoreManager.offlineMusics.first() })


    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}

    QuickSandSemiBold(
        stringResource(id = R.string.local_songs),
        size = 16,
        modifier = Modifier.padding(top = 35.dp, start = 15.dp)
    )

    Column(
        Modifier
            .padding(vertical = 15.dp, horizontal = 9.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BlackLight)
            .padding(5.dp)
    ) {
        ViewLocalSongs(
            stringResource(id = R.string.show_local_song_when_offline),
            offlineMusics == DataStoreMusicEnum.ON_OFFLINE.v
        ) {
            if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                return@ViewLocalSongs
            }
            dataStoreManager.offlineMusics = flowOf(DataStoreMusicEnum.ON_OFFLINE.v)
        }


        ViewLocalSongs(
            stringResource(id = R.string.hide_local_songs),
            offlineMusics == DataStoreMusicEnum.HIDE.v
        ) {
            dataStoreManager.offlineMusics = flowOf(DataStoreMusicEnum.HIDE.v)
        }


        ViewLocalSongs(
            stringResource(id = R.string.show_local_songs),
            offlineMusics == DataStoreMusicEnum.SHOW.v
        ) {
            if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                return@ViewLocalSongs
            }
            dataStoreManager.offlineMusics = flowOf(DataStoreMusicEnum.SHOW.v)
        }
    }

    LaunchedEffect(Unit) {
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE))
            dataStoreManager.offlineMusics = flowOf(DataStoreMusicEnum.HIDE.v)
    }
}
