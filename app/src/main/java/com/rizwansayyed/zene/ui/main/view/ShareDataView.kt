package com.rizwansayyed.zene.ui.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.view.ButtonWithBorder

@Composable
fun ShareDataView(data: ZeneMusicData?, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Row(
                Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                ButtonWithBorder(R.string.items) { }
                ButtonWithBorder(R.string.items) { }
                ButtonWithBorder(R.string.items) { }
                ButtonWithBorder(R.string.items) { }
                ButtonWithBorder(R.string.items) { }
                ButtonWithBorder(R.string.items) { }
                ButtonWithBorder(R.string.items) { }
            }
        }
    }
}