package com.rizwansayyed.zene.ui.connect.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins

@Composable
fun ZeneConnectButtonView(vibesView: () -> Unit) {
    Spacer(Modifier.height(100.dp))

    val cameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) vibesView()
        }

    LazyVerticalGrid(GridCells.Fixed(3), Modifier.heightIn(max = 400.dp)) {
        item {
            ButtonOfOptions(R.drawable.ic_add, R.string.send_vibes) {
                cameraPermission.launch(Manifest.permission.CAMERA)
            }
        }

        item {
            ButtonOfOptions(R.drawable.ic_setting, R.string.vibes_settings) {

            }
        }

        item {

        }
    }
}

@Composable
fun ButtonOfOptions(img: Int, txt: Int, click: () -> Unit) {
    Column(
        Modifier
            .padding(horizontal = 12.dp)
            .clickable {
                click()
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .padding(bottom = 4.dp)
                .border(2.dp, Color.White, RoundedCornerShape(100))
                .size(70.dp), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            ImageIcon(img, 30)
        }
        TextPoppins(stringResource(txt), false, size = 15, limit = 1)
    }
}