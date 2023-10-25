package com.rizwansayyed.zene.presenter.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin

@Composable
fun SimpleTextDialog(
    title: String, body: String, btn: String,
    onConfirmation: () -> Unit, onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            TextRegular(title)
        },
        text = {
            TextThin(body)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                TextSemiBold(btn)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                TextSemiBold(stringResource(R.string.cancel))
            }
        },
        containerColor = MainColor
    )
}