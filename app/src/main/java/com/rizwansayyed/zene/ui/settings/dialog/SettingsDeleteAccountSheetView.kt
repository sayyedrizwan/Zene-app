package com.rizwansayyed.zene.ui.settings.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.settings.view.logout
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.openFeedbackMail
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDeleteAccountSheetView(close: () -> Unit) {
    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), contentColor = MainColor, containerColor = MainColor
    ) {
        val homeViewModel: HomeViewModel = hiltViewModel()
        val context = LocalContext.current
        var successAlert by remember { mutableStateOf(false) }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp),
            Arrangement.Center, Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.Start) {
                TextViewSemiBold(stringResource(R.string.delete_account))
                TextViewNormal(stringResource(R.string.delete_account_desc))
            }

            Spacer(Modifier.height(30.dp))

            ButtonHeavy(stringResource(R.string.share_feedback), BlackGray) {
                openFeedbackMail()
                close()
            }

            Spacer(Modifier.height(30.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                Box(Modifier.clickable {
                    close()
                }) {
                    TextViewNormal(stringResource(R.string.close))
                }
                Box(Modifier.clickable {
                    homeViewModel.deleteAccount()
                    successAlert = true

                }) {
                    TextViewNormal(stringResource(R.string.delete))
                }
            }

            Spacer(Modifier.height(70.dp))
        }


        if (successAlert) TextAlertDialog(
            R.string.account_deletion_scheduled,
            null, R.string.account_deletion_scheduled_desc
        ) {
            logout(context)
            close()
        }
    }
}