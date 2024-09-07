package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R


@Composable
fun AlertDialogView(title: Int, desc: Int, btn: Int?, onDismiss: (Boolean) -> Unit) {
    AlertDialog(
        containerColor = Color.White,
        title = {
            TextPoppinsSemiBold(stringResource(title), false, Color.Black, 15)
        },
        text = {
            TextPoppins(stringResource(desc), false, Color.Black, 15)
        },
        onDismissRequest = { onDismiss(false) },
        confirmButton = {
            btn?.let { b ->
                Row(
                    Modifier
                        .padding(horizontal = 5.dp)
                        .clickable { onDismiss(true) }) {
                    TextPoppinsSemiBold(stringResource(b), false, Color.Blue, 14)
                }
            }
        },
        dismissButton = {
            Row(
                Modifier
                    .padding(horizontal = 5.dp)
                    .clickable { onDismiss(false) }) {
                TextPoppinsSemiBold(stringResource(R.string.cancel), false, Color.Blue, 14)
            }
        }
    )
}
