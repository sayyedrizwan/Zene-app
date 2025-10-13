package com.rizwansayyed.zene.ui.settings.dialog

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.pushNewsLetterDB
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun NotificationAlertSettingsView(close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val homeViewModel: HomeViewModel = hiltViewModel()

        val userInfo by userInfo.collectAsState(null)

        var newsLetterEmail by remember { mutableStateOf(true) }
        val newsLetterPush by pushNewsLetterDB.collectAsState(true)

        val coroutine = rememberCoroutineScope()

        LaunchedEffect(userInfo?.emailSubscribe) {
            newsLetterEmail = userInfo?.emailSubscribe ?: true
        }

        Box(
            Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            Column(
                Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally
            ) {
                NotificationFieldsOn(
                    R.string.suggestions_and_updates,
                    R.string.suggestions_and_updates_desc, newsLetterEmail, newsLetterPush,
                    {
                        newsLetterEmail = it
                        homeViewModel.updateEmailSubscription(it)
                    },
                    {
                        coroutine.safeLaunch {
                            pushNewsLetterDB = flowOf(it)
                        }
                    })


                Spacer(Modifier.height(50.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    Spacer(
                        Modifier
                            .fillMaxWidth(0.7f)
                            .height(0.5.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color.White)

                    )
                }

                Spacer(Modifier.height(50.dp))

                NotificationFieldsOn(
                    R.string.important_updates,
                    R.string.important_updates_desc,
                    email = true,
                    push = true,
                    emailChange = {},
                    pushChange = {})
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Spacer(Modifier.height(10.dp))

                NotificationAlertTop(close)
            }
        }
    }
}

@Composable
fun NotificationFieldsOn(
    title: Int, desc: Int, email: Boolean, push: Boolean,
    emailChange: (Boolean) -> Unit, pushChange: (Boolean) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.dp)
    ) {
        TextViewSemiBold(stringResource(title), 24)
        TextViewNormal(stringResource(desc), 15)
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 25.dp)
            .padding(horizontal = 7.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            TextViewNormal(stringResource(R.string.email_updates), 18)
        }

        Switch(
            email, emailChange, colors = SwitchDefaults.colors(
                checkedThumbColor = MainColor,
                checkedTrackColor = Color.White,
                uncheckedThumbColor = MainColor,
                uncheckedTrackColor = Color.Gray,
            )
        )
    }


    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(horizontal = 7.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            TextViewNormal(stringResource(R.string.push_notifications), 18)
        }

        Switch(
            push, pushChange, colors = SwitchDefaults.colors(
                checkedThumbColor = MainColor,
                checkedTrackColor = Color.White,
                uncheckedThumbColor = MainColor,
                uncheckedTrackColor = Color.Gray,
            )
        )
    }
}

@Composable
fun NotificationAlertTop(close: () -> Unit) {
    Box(Modifier.padding(9.dp), Alignment.Center) {
        Box(
            Modifier
                .rotate(180f)
                .align(Alignment.CenterStart)
                .clickable {
                    close()
                }) {
            ImageIcon(R.drawable.ic_arrow_right, 28)
        }

        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
            TextViewBold(stringResource(R.string.notification_preferences), 19)
        }
    }
}