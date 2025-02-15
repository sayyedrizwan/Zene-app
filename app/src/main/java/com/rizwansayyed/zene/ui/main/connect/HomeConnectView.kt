package com.rizwansayyed.zene.ui.main.connect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking.Companion.updateLocationLat
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking.Companion.updateLocationLon
import com.rizwansayyed.zene.ui.main.connect.connectview.ConnectStatusView
import com.rizwansayyed.zene.ui.main.connect.view.LocationPermissionView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.MainUtils.isLocationPermissionGranted
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeConnectView() {
    val coroutines = rememberCoroutineScope()
    val connectViewModel: ConnectViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext
    val backgroundLocation by lazy { BackgroundLocationTracking(context) }

    var locationPermission by remember { mutableStateOf(false) }
    var isEveryShownLocationPermission by remember { mutableStateOf(false) }
    var job by remember { mutableStateOf<Job?>(null) }

    var locationZoom by remember { mutableFloatStateOf(9f) }
    var currentLatLngUser by remember { mutableStateOf<LatLng?>(null) }
    var currentLatLng by remember { mutableStateOf<LatLng?>(null) }
    var cameraPositionState by remember { mutableStateOf<CameraPositionState?>(null) }

    BottomSheetScaffold(
        { ConnectStatusView(connectViewModel) },
        Modifier.fillMaxSize(),
        sheetPeekHeight = 280.dp,
        sheetContentColor = MainColor,
        sheetContainerColor = MainColor
    ) {
        if (currentLatLng != null) {
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLatLng!!, locationZoom)
            }

            var properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

            GoogleMap(
                Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState!!,
                properties = properties
            ) {
                currentLatLngUser?.let { it1 -> MainMarkerView(it1) }
                when (val v = connectViewModel.connectUserList) {
                    ResponseResult.Empty -> {}
                    is ResponseResult.Error -> {}
                    ResponseResult.Loading -> {}
                    is ResponseResult.Success -> v.data.forEach { u ->
                        if (u.user?.isUserLocation() == true) UsersMainMarkerView(u.user)
                    }
                }
            }


            LaunchedEffect(Unit) {
                snapshotFlow { cameraPositionState!!.position.zoom }.collect { zoom ->
                    properties = if (zoom > 16) properties.copy(mapType = MapType.HYBRID)
                    else properties.copy(mapType = MapType.NORMAL)
                }
            }
        }

        LifecycleResumeEffect(Unit, locationPermission) {
            job?.cancel()
            if (isLocationPermissionGranted()) coroutines.launch(Dispatchers.IO) {
                backgroundLocation.start()
                currentLatLngUser = LatLng(updateLocationLat, updateLocationLon)
                currentLatLng = LatLng(updateLocationLat, updateLocationLon)
                locationZoom = 14f

                job = coroutines.launch(Dispatchers.IO) {
                    while (true) {
                        if (currentLatLng?.latitude == 0.0 && currentLatLng?.longitude == 0.0) {
                            currentLatLng = null
                            delay(1.seconds)
                            currentLatLng = LatLng(updateLocationLat, updateLocationLon)
                        }

                        currentLatLngUser = LatLng(updateLocationLat, updateLocationLon)
                        delay(3.seconds)
                    }
                }

                if (isActive) cancel()
            } else coroutines.launch(Dispatchers.IO) {
                val ip = ipDB.firstOrNull()
                currentLatLng = LatLng(ip?.lat ?: 0.0, ip?.lon ?: 0.0)
                locationZoom = 11f
                if (!isEveryShownLocationPermission) locationPermission = true
                if (isActive) cancel()
            }

            onPauseOrDispose {
                backgroundLocation.stop()
                job?.cancel()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { connectViewModel.clear() }
    }

    if (locationPermission) {
        isEveryShownLocationPermission = true
        LocationPermissionView {
            backgroundLocation.start()
            locationPermission = false
        }
    }
}