package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.Utils
import kotlinx.coroutines.tasks.await

@Composable
fun AppUpdateView() {
    val context = LocalContext.current.applicationContext
    val appUpdateManager = AppUpdateManagerFactory.create(context)
    var dialog by remember { mutableStateOf(false) }

    if (dialog)
        AlertDialog(
            { dialog = false },
            title = {
                TextRegular(stringResource(R.string.update_available))
            },
            text = {
                TextThin(stringResource(R.string.update_available_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialog = false
                        Utils.openAppOnPlayStore()
                    }
                ) {
                    TextSemiBold(stringResource(R.string.update))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialog = false
                    }
                ) {
                    TextSemiBold(stringResource(R.string.cancel))
                }
            },
            containerColor = MainColor,
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )

    LaunchedEffect(Unit) {
        try {
            val appUpdateInfo = appUpdateManager.appUpdateInfo.await()
            val up = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val f = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)

            if (up && f) dialog = true

        } catch (e: Exception) {
            e.message
        }
    }
}
