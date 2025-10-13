package com.rizwansayyed.zene.ui.main.connect.connectchat

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.connect_status.view.AddJamDialog
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibingSnapAlertNew
import com.rizwansayyed.zene.ui.connect_status.view.GifAlert
import com.rizwansayyed.zene.ui.connect_status.view.permissions
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectChatViewModel
import com.rizwansayyed.zene.viewmodel.ConnectSocketChatViewModel
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.GifViewModel
import java.io.File
import java.util.Locale


@Composable
fun ConnectProfileMessageView(
    viewModel: ConnectChatViewModel,
    user: ConnectUserInfoResponse,
    connectChatSocket: ConnectSocketChatViewModel
) {
    val connectViewModel: ConnectViewModel = hiltViewModel()
    val gifViewModel: GifViewModel = hiltViewModel()

    var messageText by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    var showJamAlert by remember { mutableStateOf(false) }
    var showGifAlert by remember { mutableStateOf(false) }
    var showTimerAlert by remember { mutableStateOf(false) }

    var isTyping by remember { mutableStateOf(false) }
    val handler = remember { Handler(Looper.getMainLooper()) }
    val typingTimeout = 1000L

    val typingRunnable = remember { Runnable { isTyping = false } }

    LaunchedEffect(isTyping) { connectChatSocket.typingMessage(isTyping) }

    val needPermission = stringResource(R.string.need_camera_microphone_permission_to_photo)
    val fileSizeIsMore = stringResource(R.string.th_file_is_large_max_size_is_20_mb)

    val request =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.any { v -> !v.value }) {
                openAppSettings()
                needPermission.toast()
            } else {
                showAlert = true
            }
        }


    val pickFile =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val file = getFileFromUri(it)
                file?.let { pickedFile ->
                    if (isFileSizeValid(pickedFile))
                        viewModel.sendFileMessage(user.user?.email, pickedFile)
                    else fileSizeIsMore.toast()
                }
            }
        }


    Row(
        Modifier
            .background(MainColor)
            .padding(bottom = 15.dp)
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .imePadding(), Arrangement.Center, Alignment.CenterVertically
    ) {
        TextField(
            messageText,
            {
                messageText = if (it.length <= 250) it else it.take(250)
                if (!isTyping) {
                    isTyping = true
                }
                handler.removeCallbacks(typingRunnable)
                handler.postDelayed(typingRunnable, typingTimeout)
            },
            Modifier
                .padding(horizontal = 5.dp)
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions {
                viewModel.sendConnectMessage(user.user?.email, messageText, null)
                messageText = ""
            },
            placeholder = {
                TextViewNormal(
                    stringResource(R.string.enter_your_message), 14, color = Color.Black, line = 1
                )
            },
            trailingIcon = {
                if (messageText.isNotEmpty()) {
                    Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
                        IconButton({
                            viewModel.sendConnectMessage(user.user?.email, messageText, null)
                            messageText = ""
                        }) {
                            ImageIcon(R.drawable.ic_sent, 24, Color.Black)
                        }
                        Spacer(Modifier.width(1.dp))
                        IconButton({
                            showTimerAlert = true
                        }) {
                            ImageIcon(R.drawable.ic_timer, 24, Color.Black)
                        }
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            singleLine = true,
            maxLines = 1
        )

        if (viewModel.sendConnectMessageLoading) CircularLoadingViewSmall()

        AnimatedVisibility(messageText.isEmpty()) {
            Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
                ImageWithBgRound(R.drawable.ic_camera) {
                    request.launch(permissions.toTypedArray())
                }

                ImageWithBgRound(R.drawable.ic_gif) {
                    showGifAlert = true
                }

                ImageWithBgRound(R.drawable.ic_music_note) {
                    showJamAlert = true
                }

                ImageWithBgRound(R.drawable.ic_file_add) {
                    pickFile.launch("*/*")
                }
            }
        }
    }

    if (showGifAlert) GifAlert(null, gifViewModel) {
        if (it != null)
            viewModel.sendConnectMessage(user.user?.email, messageText, it)

        showGifAlert = false
    }

    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ConnectVibingSnapAlertNew(connectViewModel) {
            showAlert = false
        }
    }

    if (showJamAlert) Dialog(
        { showJamAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AddJamDialog {
            viewModel.sendConnectJamMessage(user.user?.email, it)
            showJamAlert = false
        }
    }

    if (showTimerAlert) {
        val days = messageExpiryTime.first { user.expireInMinutes == it.minutes }

        val expireTitle = String
            .format(
                Locale.getDefault(),
                stringResource(R.string.message_expire_after_),
                "(${days.text})"
            )
        TextAlertDialog(null, expireTitle, R.string.message_expire_after_desc) {
            showTimerAlert = false
        }
    }

    LaunchedEffect(connectViewModel.connectFileSelected?.media) {
        val file = connectViewModel.connectFileSelected?.media
        val thumbnail = connectViewModel.connectFileSelected?.media_thubnail
        if (File(file.toString()).exists())
            viewModel.sendImageVideo(user.user?.email, file, thumbnail)
    }
}


private fun isFileSizeValid(file: File): Boolean {
    val sizeInBytes = file.length()
    val sizeInMb = sizeInBytes / (1024 * 1024)

    return sizeInMb <= 20
}

fun getFileFromUri(uri: Uri): File? {
    return try {
        val fileName = getFileName(uri) ?: "temp_file"
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, fileName)
        file.deleteRecursively()
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun getFileName(uri: Uri): String? {
    var name: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) {
                name = it.getString(index)
            }
        }
    }
    return name
}