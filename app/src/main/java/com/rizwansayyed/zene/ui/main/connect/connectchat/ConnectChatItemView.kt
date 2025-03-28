package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.data.model.getShowFullTS
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibeMediaItemAlert
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.DownloadInformation
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.downloadViewMap
import com.rizwansayyed.zene.viewmodel.ConnectSocketChatViewModel
import java.util.Locale

@Composable
fun ConnectChatItemView(
    data: ConnectChatMessageResponse,
    otherUser: ConnectUserResponse?,
    userInfo: UserInfoResponse?,
    connectSocketViewModel: ConnectSocketChatViewModel,
    delete: () -> Unit
) {
    var showInfoOf by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 11.dp),
        Arrangement.End,
        Alignment.Bottom
    ) {
        if (data.from == otherUser?.email) {
            ProfileImageOfUser(otherUser?.profile_photo, otherUser?.name) {
                showInfoOf = !showInfoOf
            }
            MessageItemView(data, true, showInfoOf, connectSocketViewModel)
        }

        Spacer(Modifier.weight(1f))

        if (data.from == userInfo?.email) {
            MessageItemView(data, false, showInfoOf, connectSocketViewModel)
            Column(Modifier, Arrangement.Center, Alignment.CenterHorizontally) {
                if (showInfoOf) Box(Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        delete()
                    }
                    .padding(vertical = 10.dp)) {
                    ImageIcon(R.drawable.ic_delete, 20)
                }
                ProfileImageOfUser(userInfo?.photo, userInfo?.name) {
                    showInfoOf = !showInfoOf
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MessageItemView(
    data: ConnectChatMessageResponse,
    isSender: Boolean, showInfoOf: Boolean, viewModel: ConnectSocketChatViewModel
) {
    var showMediaDialog by remember { mutableStateOf(false) }

    val candidMeaning = stringResource(R.string.candid_desc)

    BoxWithConstraints(Modifier) {
        val maxWidth = maxWidth * 0.6f

        Column(Modifier.widthIn(max = maxWidth)) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .background(
                        color = BlackTransparent, shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomEnd = if (isSender) 16.dp else 0.dp,
                            bottomStart = if (isSender) 0.dp else 16.dp
                        )
                    )
                    .padding(12.dp)
                    .padding(horizontal = 12.dp)
            ) {
                if (data.file_path != null && data.file_name != null) {
                    FileDownloaderItemView(data)
                } else if (data.getMusicData() != null) {
                    ItemCardView(data.getMusicData())
                } else if (data.candid_media != null) {
                    Column {
                        Box(Modifier.clickable { showMediaDialog = true }, Alignment.Center) {
                            if (data.candid_thumbnail != null) GlideImage(
                                data.candid_thumbnail,
                                "",
                                Modifier.height(250.dp)
                            )
                            else GlideImage(data.candid_media, "", Modifier.height(250.dp))

                            if (data.candid_media.contains(".mp4")) ImageWithBgRound(R.drawable.ic_play) {
                                showMediaDialog = true
                            }
                        }
                        Spacer(Modifier.width(7.dp))
                        Box(Modifier.clickable { candidMeaning.toast() }) {
                            TextViewSemiBold(stringResource(R.string.candid), 14, Color.White)
                        }
                    }
                } else if (data.gif != null) GlideImage(
                    data.gif, data.message, Modifier.fillMaxWidth()
                )
                else TextViewBold(data.message ?: "", 16, Color.White)
            }

            AnimatedVisibility(showInfoOf) {
                ShowChatInfo(data, !isSender, viewModel)
            }
        }
    }

    if (showMediaDialog) ConnectVibeMediaItemAlert(data.candid_media) {
        showMediaDialog = false
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserTypingAnimation(user: ConnectUserResponse?) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 11.dp),
        Arrangement.End,
        Alignment.Bottom
    ) {
        ProfileImageOfUser(user?.profile_photo, user?.name) {}

        Box(
            modifier = Modifier
                .padding(horizontal = 7.dp)
                .background(
                    color = BlackTransparent, shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 0.dp
                    )
                )
                .padding(12.dp)
                .padding(horizontal = 12.dp),
        ) {
            GlideImage(
                R.raw.typing_animation,
                "typing",
                Modifier.size(55.dp, 39.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.weight(1f))

    }
}

@Composable
fun ShowChatInfo(
    data: ConnectChatMessageResponse,
    isMyChat: Boolean,
    viewModel: ConnectSocketChatViewModel
) {
    Column {
        Row(Modifier.padding(horizontal = 15.dp), Arrangement.End, Alignment.CenterVertically) {
            if (isMyChat) Spacer(Modifier.weight(1f))
            if (isMyChat) {
                if (viewModel.inLobby) {
                    TextViewNormal(stringResource(R.string.seen), 15)
                } else {
                    if (data.did_read == true) TextViewNormal(stringResource(R.string.seen), 15)
                    else TextViewNormal(stringResource(R.string.not_seen), 15)
                }
            }

            if (isMyChat) TextViewNormal(" • ", 15)

            getShowFullTS(data.timestamp)?.let {
                TextViewNormal(it, 14)
            }

            if (!isMyChat) Spacer(Modifier.weight(1f))
        }
        Row(Modifier.padding(horizontal = 15.dp), Arrangement.End, Alignment.CenterVertically) {
            getShowFullTS(data.expire_at)?.let {
                TextViewNormal(String.format(Locale.getDefault(), stringResource(R.string.expire_at), it), 14)
            }
        }
    }
}


@Composable
fun FileDownloaderItemView(data: ConnectChatMessageResponse) {
    val filePath = remember { mutableStateOf(downloadViewMap[data.file_path]) }
    val downloadInformation by remember { derivedStateOf { filePath.value?.downloadProgress } }
    val downloadComplete by remember { derivedStateOf { filePath.value?.isDownloadComplete } }

    Row(Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        if (data.file_path != null && filePath.value == null) {
            downloadViewMap[data.file_path] =
                DownloadInformation(data.file_path, data.file_name ?: "")
            filePath.value = downloadViewMap[data.file_path]
        } else if (downloadComplete == true) {
            filePath.value?.openDownloadFolder()
        }
    }, Arrangement.Center, Alignment.CenterVertically
    ) {
        ImageIcon(R.drawable.ic_folder_download, 28)

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 7.dp)
        ) {
            TextViewBold(data.file_name ?: "", 15, Color.White)
            if (downloadComplete == null) {
                TextViewSemiBold(data.fileSize(), 12, Color.White)
            } else if (downloadComplete == true) {
                TextViewSemiBold(stringResource(R.string.downloaded), 12, Color.White)
            } else if (downloadInformation != null) {
                TextViewSemiBold("${downloadInformation}%", 12, Color.White)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImageOfUser(profilePhoto: String?, name: String?, click: () -> Unit) {
    GlideImage(
        profilePhoto, name, Modifier
            .clip(RoundedCornerShape(100))
            .clickable { click() }
            .size(35.dp)
    )
}