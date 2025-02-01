package com.rizwansayyed.zene.ui.connect_status.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.isFileExtensionVideo
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeImageFile
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeVideoCroppedFile
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeVideoFile
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import java.io.File

@Composable
fun ConnectAttachFiles(viewModel: ConnectViewModel) {
    var showAlert by remember { mutableStateOf(false) }
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val savedPath = saveFileToAppDirectory(uri)
                if (savedPath == vibeImageFile) viewModel.updateVibeFileInfo(savedPath, false)
                else showAlert = true
            }
        }

    Spacer(Modifier.height(30.dp))
    SettingsViewSimpleItems(R.drawable.ic_folder, R.string.attach_photo_video) {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        VideoEditorDialog(viewModel) {
            showAlert = false
        }
    }
}

@Composable
fun VideoEditorDialog(viewModel: ConnectViewModel, close: () -> Unit) {

    LaunchedEffect(Unit) {
        val cmd =  "-ss 00:00:05.00 -t 00:00:10.00 -noaccurate_seek -i $vibeVideoFile -codec copy -avoid_negative_ts 1 $vibeVideoCroppedFile"
        val session = FFmpegKit.execute(cmd)
        if (ReturnCode.isSuccess(session.returnCode)) "done".toast()
        else {
            "nooo".toast()
        }
    }
}

private fun saveFileToAppDirectory(uri: Uri): File {
    vibeVideoCroppedFile.delete()
    vibeVideoFile.delete()
    vibeImageFile.delete()

    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val mimeType = contentResolver.getType(uri)
    val file = if (isFileExtensionVideo(mimeType)) vibeVideoFile else vibeImageFile
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}