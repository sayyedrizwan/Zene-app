package com.rizwansayyed.zene.ui.settings.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun EditProfileUsernameDialog(viewModel: HomeViewModel, close: (Boolean) -> Unit) {
    Dialog(onDismissRequest = { close(false) }) {
        var username by remember { mutableStateOf("") }
        var isUserDifferent by remember { mutableStateOf(false) }
        val savedUsername by userInfo.collectAsState(null)
        val enterYourUsername = stringResource(R.string.enter_a_username)
        val focusManager = LocalFocusManager.current

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .clip(RoundedCornerShape(16.dp))
                .background(MainColor), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            TextViewNormal(enterYourUsername, 16)
            Spacer(Modifier.height(10.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp), Arrangement.SpaceEvenly, Alignment.CenterVertically
            ) {
                TextField(
                    username, {
                        if (it.length <= 38) {
                            username = it.lowercase().trim()
                            if (it.trim().length >= 3) viewModel.checkUsername(it.lowercase().trim())
                        }
                    },
                    Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions { focusManager.clearFocus() },
                    textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 17.sp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = BlackGray,
                        unfocusedContainerColor = BlackGray,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White
                    ),
                    singleLine = true
                )
                Spacer(Modifier.width(4.dp))

                if (username.trim() != savedUsername?.username)
                    when (val v = viewModel.checkUsernameInfo) {
                        ResponseResult.Empty -> {}
                        is ResponseResult.Error -> {}
                        ResponseResult.Loading -> CircularLoadingViewSmall()
                        is ResponseResult.Success -> if (username.trim().length > 3) {
                            isUserDifferent = v.data
                            if (v.data)
                                ImageIcon(R.drawable.ic_tick, 19, Color.Green)
                            else
                                ImageIcon(R.drawable.ic_cancel_close, 19, Color.Red)
                        }
                    }
            }
            Spacer(Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                ButtonWithBorder(R.string.close) {
                    close(false)
                }

                if (username.trim().length > 3 && isUserDifferent) {
                    Spacer(Modifier.width(10.dp))
                    ButtonWithBorder(R.string.save) {
                        viewModel.updateUsername(username)
                        close(true)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
        }

        LaunchedEffect(Unit) {
            username = userInfo.firstOrNull()?.username ?: ""
        }
    }
}