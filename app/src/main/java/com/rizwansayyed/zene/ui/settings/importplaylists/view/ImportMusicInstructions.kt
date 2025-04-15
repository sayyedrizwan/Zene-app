package com.rizwansayyed.zene.ui.settings.importplaylists.view

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.connect.connectchat.getFileFromUri
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.URLSUtils.TUNE_MY_MUSIC_TRANSFER
import com.rizwansayyed.zene.viewmodel.ImportPlaylistViewModel
import java.io.File

@Composable
fun ImportMusicInstructions(viewModel: ImportPlaylistViewModel) {
    val context = LocalContext.current.applicationContext
    val pickCsvLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let {
                val file = getFileFromUri(it)
                file?.let { f -> viewModel.setFile(f) }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        TextViewBold(stringResource(R.string.how_to_import_music_via_tune_my_music), 25)

        Spacer(modifier = Modifier.height(10.dp))

        InstructionStep(1, R.string.tune_my_music_transfer_1)
        InstructionStep(2, R.string.tune_my_music_transfer_2)
        InstructionStep(3, R.string.tune_my_music_transfer_3)
        InstructionStep(4, R.string.tune_my_music_transfer_4)
        InstructionStep(5, R.string.tune_my_music_transfer_6)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, TUNE_MY_MUSIC_TRANSFER.toUri())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonColors(MainColor, MainColor, MainColor, MainColor)
        ) {
            TextViewSemiBold(stringResource(R.string.open_tune_my_music), 17)
        }

        Spacer(modifier = Modifier.height(23.dp))

        Button(
            onClick = {
                pickCsvLauncher.launch(arrayOf(
                    "text/csv",
                    "text/comma-separated-values",
                    "application/csv",
                    "application/vnd.ms-excel"
                ))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonColors(MainColor, MainColor, MainColor, MainColor)
        ) {
            TextViewSemiBold(stringResource(R.string.select_csv), 17)
        }
    }
}

@Composable
fun InstructionStep(number: Int, text: Int) {
    Row(
        verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Box(Modifier.padding(end = 8.dp)) {
            TextViewBold("$number.", size = 16)
        }
        TextViewNormal(stringResource(text), size = 16)
    }
}
