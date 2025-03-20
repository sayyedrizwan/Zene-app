package com.rizwansayyed.zene.ui.main.connect.profile

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.FRIENDS
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.getAddressFromLatLong
import com.rizwansayyed.zene.viewmodel.ConnectViewModel

@SuppressLint("MissingPermission")
@Composable
fun ConnectLocationButton(
    user: ConnectUserInfoResponse, viewModel: ConnectViewModel, close: () -> Unit
) {
    var areaName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    TextViewBold(stringResource(R.string.send_location), 19, Color.White)
    Spacer(Modifier.height(25.dp))

    TextViewNormal(areaName, 16, Color.White, true)
    Spacer(Modifier.height(45.dp))

    if (isLoading) CircularLoadingView()

    if (areaName.trim().length > 2) ButtonWithImageAndBorder(
        R.drawable.ic_location, R.string.send_location, Color.White, Color.White
    ) {
        viewModel.sendConnectLocation(user.user?.email)
        close()
    }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val location = BackgroundLocationTracking.getLatestLocation()
            areaName = try {
                getAddressFromLatLong(location!!.latitude, location.longitude) ?: ""
            } catch (e: Exception) {
                ""
            }
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
        }
    }

    Spacer(Modifier.height(80.dp))
}