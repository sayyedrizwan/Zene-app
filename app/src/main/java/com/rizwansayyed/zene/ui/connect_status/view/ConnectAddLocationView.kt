package com.rizwansayyed.zene.ui.connect_status.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.ui.main.connect.view.LocationPermissionView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await

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
        ConnectAddLocationDialog {}
    }


    if (permissionAlert) LocationPermissionView {
        if (permissionAlert) showAlert = true
        permissionAlert = false
    }
}


@SuppressLint("MissingPermission")
@Composable
fun ConnectAddLocationDialog(close: () -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext
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
                    if (search.length > 3) viewModel.searchZene(search)
                },
                placeholder = {
                    TextViewNormal(stringResource(R.string.search_places), 14)
                },
                trailingIcon = {
                    if (search.length > 3) {
                        IconButton({
                            focusManager.clearFocus()
                            viewModel.searchZene(search)
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
    }

    LaunchedEffect(Unit) {
        val l = LocationServices.getFusedLocationProviderClient(context)
        try {
            val token = CancellationTokenSource().token
            val location = l.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token).await()
            viewModel.searchPlaces(location.latitude, location.longitude)
        } catch (e: Exception) {
            val ip = ipDB.firstOrNull()
            if (ip?.lat != null && ip.lon != null) viewModel.searchPlaces(ip.lat, ip.lon)
        }
    }
}