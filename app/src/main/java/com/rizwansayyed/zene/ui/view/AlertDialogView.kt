package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R

@Composable
fun TextAlertDialog(title: Int, name: Int, dismiss: () -> Unit, click: () -> Unit) {
    AlertDialog(title = {
        TextViewNormal(stringResource(title), 17, line = 2, center = false)
    }, text = {
        TextViewNormal(stringResource(name), 16, center = false)
    }, onDismissRequest = {
        dismiss()
    }, confirmButton = {
        TextButton(onClick = {
            click()
        }) {
            TextViewLight(stringResource(R.string.confirm), 15)
        }
    }, dismissButton = {
        TextButton(onClick = {
            dismiss()
        }) {
            TextViewLight(stringResource(R.string.cancel), 15)
        }
    })
}

@Composable
fun TextAlertDialog(title: Int, name: Int, dismiss: () -> Unit) {
    AlertDialog(title = {
        TextViewNormal(stringResource(title), 17, line = 2, center = false)
    }, text = {
        TextViewNormal(stringResource(name), 16, center = false)
    }, onDismissRequest = {
        dismiss()
    }, confirmButton = {}, dismissButton = {
        TextButton(onClick = {
            dismiss()
        }) {
            TextViewLight(stringResource(R.string.cancel), 15)
        }
    })
}