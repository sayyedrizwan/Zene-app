package com.rizwansayyed.zene.ui.main.connect.connectchat

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.FRIENDS
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.utils.ChatTempDataUtils.currentOpenedChatProfile
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun ConnectProfileMessagingView(
    user: ConnectUserInfoResponse, viewModel: ConnectViewModel, close: () -> Unit
) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val window = LocalActivity.current?.window
        var job by remember { mutableStateOf<Job?>(null) }
        val coroutines = rememberCoroutineScope()
        val state = rememberLazyListState()
        val userInfo by DataStorageManager.userInfo.collectAsState(null)
        var isAtBottom by remember { mutableStateOf(true) }

        Column(
            Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            ConnectTopProfileView(Modifier.weight(1f), user, close)

            LazyColumn(Modifier.weight(8f), state, verticalArrangement = Arrangement.Bottom) {
                items(viewModel.recentChatItems, key = { it._id!! }) {
                    ConnectChatItemView(it, user.user, userInfo)
                }

                item(key = "bottom") {
                    Spacer(Modifier.height(30.dp))
                    DisposableEffect(Unit) {
                        isAtBottom = true
                        onDispose { isAtBottom = false }
                    }
                }
            }

            if (user.isConnected() == FRIENDS) Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ConnectProfileMessageView(viewModel, user)
            }
        }

        LaunchedEffect(viewModel.recentChatItems.map { it._id }) {
            Log.d("TAG", "ConnectProfileMessagingView: data ")
            if (isAtBottom) state.animateScrollToItem(state.layoutInfo.totalItemsCount)
        }

        @Suppress("DEPRECATION") LifecycleResumeEffect(Unit) {
            window?.statusBarColor = MainColor.toArgb()
            window?.navigationBarColor = MainColor.toArgb()
            currentOpenedChatProfile = user.user?.email

            job?.cancel()
            job = coroutines.launch {
                while (true) {
                    viewModel.getChatConnectRecentMessage(user.user?.email)
                    delay(2.seconds)
                }
            }

            viewModel.markConnectMessageToRead(user.user?.email)

            onPauseOrDispose {
                window?.statusBarColor = Color.Transparent.toArgb()
                window?.navigationBarColor = Color.Transparent.toArgb()
                currentOpenedChatProfile = null
                viewModel.markConnectMessageToRead(user.user?.email)
                user.user?.email?.let { viewModel.connectUserInfo(it) }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullChatItemView(
    data: ConnectChatMessageResponse, otherUser: ConnectUserResponse?, userInfo: UserInfoResponse?
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 11.dp)
    ) {
        if (data.from == otherUser?.email) {
            GlideImage(
                otherUser?.profile_photo,
                otherUser?.name,
                Modifier
                    .clip(RoundedCornerShape(100))
                    .size(30.dp)
            )
        }
        Spacer(Modifier.weight(1f))
        if (data.from == userInfo?.email) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val maxWidth = maxWidth * 0.6f
                Row(Modifier.fillMaxWidth(), Arrangement.End, Alignment.Bottom) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                            .background(
                                color = Color(0xFFFFFFFF),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomEnd = 0.dp,
                                    bottomStart = 16.dp
                                )
                            )
                            .padding(12.dp)
                            .widthIn(max = maxWidth)
                    ) {
                        TextViewBold(data.message ?: "", 16, Color.Black, center = false)
                    }
                    GlideImage(
                        userInfo?.photo, userInfo?.name, Modifier
                            .clip(RoundedCornerShape(100))
                            .size(30.dp)
                    )
                }
            }
//            TextViewNormal(data.message ?: "", 16, Color.White, center = false)
        }
    }
}