package com.rizwansayyed.zene.ui.main.connect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.ui.main.connect.view.LocationPermissionView
import com.rizwansayyed.zene.utils.MainUtils.isLocationPermissionGranted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun HomeConnectView() {
    val coroutines = rememberCoroutineScope()
    var locationPermission by remember { mutableStateOf(false) }

    var locationZoom by remember { mutableFloatStateOf(9f) }
    var currentLatLng by remember { mutableStateOf<LatLng?>(null) }
    if (currentLatLng != null) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(currentLatLng!!, locationZoom)
        }

        Box(Modifier.fillMaxSize()) {
            GoogleMap(Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
//            Marker(
//                state = singaporeMarkerState, title = "Singapore", snippet = "Marker in Singapore"
//            )
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        if (isLocationPermissionGranted()) coroutines.launch(Dispatchers.IO) {

            if (isActive) cancel()
        } else coroutines.launch(Dispatchers.IO) {
            val ip = ipDB.firstOrNull()
            currentLatLng = LatLng(ip?.lat ?: 0.0, ip?.lon ?: 0.0)
            locationZoom = 11f
            locationPermission = true
            if (isActive) cancel()
        }
        onPauseOrDispose {}
    }

    if (locationPermission) LocationPermissionView {
        locationPermission = false
    }
}