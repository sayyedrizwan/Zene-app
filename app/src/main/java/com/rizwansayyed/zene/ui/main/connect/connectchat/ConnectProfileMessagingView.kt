package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.FRIENDS
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.ChatTempDataUtils.clearAMessage
import com.rizwansayyed.zene.utils.ChatTempDataUtils.currentOpenedChatProfile
import com.rizwansayyed.zene.utils.NotificationUtils.Companion.clearConversationNotification
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
                if (viewModel.isRecentChatLoading == 1) item {
                    CircularLoadingView()
                }

                if (viewModel.isRecentChatLoading == 2 && viewModel.recentChatItems.isEmpty()) item {
                    TextViewSemiBold(
                        stringResource(R.string.break_the_ice_start_conversation),
                        center = true
                    )
                }

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
            if (isAtBottom) state.animateScrollToItem(state.layoutInfo.totalItemsCount)
        }

        @Suppress("DEPRECATION") LifecycleResumeEffect(Unit) {
            window?.statusBarColor = MainColor.toArgb()
            window?.navigationBarColor = MainColor.toArgb()
            currentOpenedChatProfile = user.user?.email

            clearAMessage(user.user?.email ?: "")
            clearConversationNotification(user.user?.email ?: "")

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
