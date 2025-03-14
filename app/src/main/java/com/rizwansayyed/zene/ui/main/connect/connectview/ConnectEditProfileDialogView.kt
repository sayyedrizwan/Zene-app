package com.rizwansayyed.zene.ui.main.connect.connectview

import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.getAllEmojis
import com.rizwansayyed.zene.utils.MainUtils.removeSpecialChars
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull


@Composable
fun UserStatusUpdateView(close: () -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    var statusText by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxWidth()
            .background(MainColor), Arrangement.Center, Alignment.Start
    ) {
        Spacer(Modifier.height(20.dp))
        Column(Modifier.padding(horizontal = 9.dp)) {
            TextViewBold(stringResource(R.string.update_status), 18)
            TextViewNormal(stringResource(R.string.update_status_desc), 15)
        }

        Spacer(Modifier.height(10.dp))
        TextField(
            statusText,
            { if (it.length <= 5) statusText = it },
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions {},
            placeholder = {
                TextViewNormal(stringResource(R.string.emoji_status), 16, center = true)
            },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 19.sp,
                fontFamily = proximanOverFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            readOnly = true,
            trailingIcon = {
                if (statusText.isNotEmpty()) {
                    IconButton({
                        val l = statusText.offsetByCodePoints(statusText.length, -1)
                        statusText = statusText.substring(0, l)
                    }) {
                        ImageIcon(R.drawable.ic_link_backward, 24, Color.White)
                    }
                }
            },
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

        LazyHorizontalGrid(
            GridCells.Fixed(4), Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            items(getAllEmojis()) {
                Box(Modifier
                    .padding(4.dp)
                    .clickable {
                        val actualLength = statusText.codePointCount(0, statusText.length)
                        if (actualLength < 5) statusText += it.trim()
                    }) {
                    TextViewNormal(it, 20)
                }
            }
        }

        Spacer(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .clipToBounds()
        )

        Box(Modifier.fillMaxWidth(), Alignment.Center) {
            ButtonWithBorder(R.string.close) {
                close()
            }
        }
        Spacer(Modifier.height(50.dp))
    }

    LaunchedEffect(Unit) {
        val user = userInfo.firstOrNull()
        statusText = user?.status ?: ""
    }

    LaunchedEffect(statusText) {
        delay(500)
        val user = userInfo.firstOrNull()
        if (user?.status != statusText) {
            viewModel.updateConnectInfo(statusText)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ConnectEditProfileView() {
    var showEmoji by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("") }
    var emailText by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }
    var nameText by remember { mutableStateOf("") }
    var usernameText by remember { mutableStateOf("") }

    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val croppedImageUri = result.uriContent
            croppedImageUri?.toString()?.let { p -> profilePic = p }
        }
    }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) cropImage.launch(
            CropImageContractOptions(
                it, CropImageOptions(fixAspectRatio = true)
            )
        )
    }


    fun openImagePicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item {
                Spacer(Modifier.height(60.dp))
                Column(
                    Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    Box(Modifier.clickable { openImagePicker() }) {
                        GlideImage(profilePic, nameText, Modifier.size(100.dp))
                        Box(Modifier
                            .align(Alignment.BottomEnd)
                            .clickable { openImagePicker() }) {
                            ImageIcon(R.drawable.ic_edit, 17, Color.White)
                        }
                    }
                    Spacer(Modifier.height(40.dp))

                    Row(Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .clickable { showEmoji = !showEmoji }
                        .padding(6.dp),
                        Arrangement.Center,
                        Alignment.CenterVertically) {
                        TextViewSemiBold(statusText.ifEmpty { "No Status" }, 15, Color.Black)
                        if (statusText.isNotEmpty()) Box(
                            Modifier
                                .padding(start = 6.dp)
                                .clickable { statusText = "" }) {
                            ImageIcon(R.drawable.ic_delete, 17, Color.Black)
                        }
                    }
                }
            }

            item {
                Box(
                    Modifier
                        .padding(top = 40.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MainColor)
                        .padding(10.dp)
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    TextViewSemiBold(emailText, 15, Color.White)
                }
            }

            item {
                TextField(
                    nameText,
                    { if (it.length <= 25) nameText = it },
                    Modifier
                        .padding(top = 10.dp)
                        .padding(10.dp)
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    placeholder = {
                        TextViewNormal(stringResource(R.string.enter_your_name), 14)
                    },
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
            }

            item {
                TextField(
                    usernameText,
                    { if (it.length <= 38) usernameText = it.removeSpecialChars() },
                    Modifier
                        .padding(top = 10.dp)
                        .padding(10.dp)
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    placeholder = {
                        TextViewNormal(stringResource(R.string.enter_your_username), 14)
                    },
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
            }
        }

        if (showEmoji) AndroidView(
            { ctx ->
                EmojiPickerView(ctx).apply {
                    emojiGridColumns = 9
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    setOnEmojiPickedListener {
                        if (statusText.length <= 4) statusText += it.emoji.trim()
                    }
                }
            },
            Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.White)
        )
    }

    LaunchedEffect(Unit) {
        val user = userInfo.firstOrNull()
        nameText = user?.name ?: ""
        profilePic = user?.photo ?: ""
        emailText = user?.email ?: ""
        usernameText = user?.username ?: ""
    }
}
