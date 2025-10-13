package com.rizwansayyed.zene.ui.connect_status.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.ui.main.connect.view.LocationPermissionView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun ConnectAddLocation(viewModel: ConnectViewModel) {
    var permissionAlert by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }

    Spacer(Modifier.height(30.dp))
    SettingsViewSimpleItems(R.drawable.ic_location, R.string.add_location) {
        if (MainUtils.isLocationPermissionGranted()) showAlert = true
        else permissionAlert = true
    }

    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ConnectAddLocationDialog {
            showAlert = false
            viewModel.updateVibeLocationInfo(it)
        }
    }

    if (permissionAlert) LocationPermissionView {
        if (permissionAlert) showAlert = true
        permissionAlert = false
    }
}


@SuppressLint("MissingPermission")
@Composable
fun ConnectAddLocationDialog(close: (SearchPlacesDataResponse) -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    var search by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        item {
            TextField(
                search,
                { if (it.length <= 20) search = it },
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                    if (search.length > 3) viewModel.searchPlaces(search)
                },
                placeholder = {
                    TextViewNormal(stringResource(R.string.search_places), 14)
                },
                trailingIcon = {
                    if (search.length > 3) {
                        IconButton({
                            focusManager.clearFocus()
                            viewModel.searchPlaces(search)
                        }) {
                            ImageIcon(R.drawable.ic_search, 24)
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
        }

        item { Spacer(Modifier.height(15.dp)) }

        when (val v = viewModel.searchPlaces) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item { CircularLoadingView() }
            is ResponseResult.Success -> items(v.data) {
                Column(
                    Modifier
                        .padding(bottom = 35.dp)
                        .clickable { close(it) }
                        .padding(horizontal = 10.dp)
                ) {
                    TextViewSemiBold(it.name ?: "", 14)
                    TextViewNormal(it.address ?: "", 14)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.searchPlaces(null)
    }
}