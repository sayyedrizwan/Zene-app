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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AlertDialogWithImage(
    thumbnail: String?,
    name: String?,
    dismiss: () -> Unit,
    click: () -> Unit
) {
    AlertDialog(icon = {
        GlideImage(
            thumbnail, name,
            Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
    }, title = {
        TextViewNormal(name ?: "", 17, line = 2, center = true)
    }, text = {
        TextViewNormal(stringResource(R.string.do_you_want_to_play_this_song), 16, center = true)
    }, onDismissRequest = {
        dismiss()
    }, confirmButton = {
        TextButton(onClick = {
            click()
        }) {
            TextViewLight(stringResource(R.string.play), 13)
        }
    }, dismissButton = {
        TextButton(onClick = {
            dismiss()
        }) {
            TextViewLight(stringResource(R.string.cancel), 13)
        }
    })
}


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
            TextViewLight(stringResource(R.string.confirm), 13)
        }
    }, dismissButton = {
        TextButton(onClick = {
            dismiss()
        }) {
            TextViewLight(stringResource(R.string.cancel), 13)
        }
    })
}