package com.rizwansayyed.zene.ui.main.connect.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.ui.main.connect.MapMarkerUI
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import kotlinx.coroutines.launch

private val mapUISettings = MapUiSettings(
    compassEnabled = false,
    myLocationButtonEnabled = false,
    mapToolbarEnabled = false,
    zoomControlsEnabled = false
)

@Composable
fun ConnectUserMapView(user: ConnectUserResponse?) {
    val coroutine = rememberCoroutineScope()
    var locationZoom by remember { mutableFloatStateOf(18f) }
    var currentLatLng by remember { mutableStateOf<LatLng?>(null) }
    if (currentLatLng != null) {
        val properties by remember { mutableStateOf(MapProperties(mapType = MapType.HYBRID)) }

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(currentLatLng!!, locationZoom)
        }

        GoogleMap(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .height(350.dp),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = mapUISettings
        ) {
            val mainUserMarker = rememberMarkerState(position = currentLatLng!!)
            MarkerComposable(
                keys = arrayOf(1), state = mainUserMarker, title = user?.name
            ) {
                MapMarkerUI(user?.profile_photo, false)
            }
        }

        Row(
            Modifier
                .padding(top = 15.dp)
                .fillMaxWidth()
        ) {
            ImageWithBorder(R.drawable.ic_play) {
                currentLatLng = LatLng(18.942506, 72.823120)
                locationZoom = 18f
                coroutine.launch {
                    val p = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(currentLatLng!!, locationZoom, 0f, 0f)
                    )
                    cameraPositionState.animate(p, 1000)
                }
            }

            ImageWithBorder(R.drawable.ic_play) {

            }
            ImageWithBorder(R.drawable.ic_play) {

            }
        }
    }
    LaunchedEffect(Unit) {
        currentLatLng = LatLng(18.942506, 72.823120)
        locationZoom = 18f
    }
}