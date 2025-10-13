package com.rizwansayyed.zene.ui.main.connect.profile

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.ui.partycall.PartyCallActivity
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel

@Composable
fun DialogConnectUserAddPlaylist(email: String?, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val viewModel: ConnectViewModel = hiltViewModel()
        val focusManager = LocalFocusManager.current
        var playlistName by remember { mutableStateOf("") }

        Column(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MainColor, MainColor, MainColor.copy(0.5f), Color.Black, Color.Black
                        )
                    )
                ), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            TextViewSemiBold(
                stringResource(R.string.give_your_playlist_a_name), 24, line = 1, center = true
            )

            Spacer(Modifier.height(10.dp))

            TextField(
                playlistName,
                { playlistName = if (it.length <= 100) it else it.take(100) },
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
                placeholder = {
                    TextViewNormal(
                        stringResource(R.string.playlist_name), 17, Color.Gray, center = true
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 17.sp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MainColor,
                    unfocusedContainerColor = MainColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                singleLine = true
            )

            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                ButtonWithBorder(R.string.cancel) { close() }
                if (playlistName.length > 3) {
                    Spacer(Modifier.width(10.dp))
                    when (viewModel.createPlaylist) {
                        ResponseResult.Empty -> ButtonWithBorder(R.string.create) {
                            if (playlistName.length <= 3) return@ButtonWithBorder
                            viewModel.createPlaylistName(email, playlistName)
                        }

                        is ResponseResult.Error -> {}
                        ResponseResult.Loading -> CircularLoadingViewSmall()
                        is ResponseResult.Success -> {
                            LaunchedEffect(Unit) {
                                close()
                            }
                        }
                    }
                }
            }


            LaunchedEffect(Unit) {
                viewModel.createPlaylistName(null, "")
            }
        }
    }
}

@Composable
fun DialogPlaylistSyncInfo(click: (Boolean) -> Unit) {
    AlertDialog(
        icon = {
            Icon(painterResource(R.drawable.ic_user_switch), "", Modifier.size(50.dp), Color.White)
        },
        title = {
            Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
                TextViewBold(stringResource(R.string.zene_duo_shared_playlists), 14)
            }
        },
        text = {
            TextViewNormal(stringResource(R.string.zene_duo_shared_playlists_desc), 14)
        },
        onDismissRequest = { click(false) },
        confirmButton = {
            TextButton({ click(true) }) {
                TextViewNormal(stringResource(R.string.create))
            }
        },
        dismissButton = {
            TextButton({ click(false) }) {
                TextViewNormal(stringResource(R.string.cancel))
            }
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DialogPartyInfo(data: ConnectUserInfoResponse, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val context = LocalContext.current.applicationContext
        val needMicrophone = stringResource(R.string.need_microphone_permission_to_speak)

        val m =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it.all { v -> v.value }) {
                    Intent(context, PartyCallActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra(Intent.EXTRA_EMAIL, data.user?.email)
                        putExtra(Intent.EXTRA_USER, data.user?.profile_photo)
                        putExtra(Intent.EXTRA_PACKAGE_NAME, data.user?.name)
                        putExtra(Intent.EXTRA_MIME_TYPES, -1)
                        context.startActivity(this)
                    }
                    close()
                } else {
                    needMicrophone.toast()
                    openAppSettings()
                }
            }


        Column(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(MainColor)
                .padding(16.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                GlideImage(
                    R.drawable.user_sitting_with_headphone,
                    "",
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                )

                Column(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    BlackTransparent.copy(0.2f),
                                    BlackTransparent.copy(0.5f),
                                    BlackTransparent.copy(0.7f),
                                    Color.Black,
                                    Color.Black,
                                    Color.Black
                                )
                            )
                        ), Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    TextViewSemiBold(stringResource(R.string.start_a_live_party), 27, center = true)
                    Spacer(Modifier.height(1.dp))
                    TextViewNormal(
                        stringResource(R.string.start_a_live_party_desc), 14, center = true
                    )
                }

            }
            Spacer(Modifier.height(15.dp))
            ButtonHeavy(stringResource(R.string.start), Color.Black) {
                m.launch(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA))
            }
            Spacer(Modifier.height(15.dp))
            ButtonHeavy(stringResource(R.string.close), Color.Black) {
                close()
            }
        }
    }
}
