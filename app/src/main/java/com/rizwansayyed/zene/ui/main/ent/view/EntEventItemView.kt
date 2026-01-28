package com.rizwansayyed.zene.ui.main.ent.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.EventsResponsesItems
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.main.ent.EventListOverlay
import com.rizwansayyed.zene.ui.main.ent.getCircularMarkerBitmap
import com.rizwansayyed.zene.ui.main.ent.getCurrentLocationSuspend
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


@Composable
fun EventAllEventsLists() {

}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventsMapScreen(events: List<EventsResponsesItems>, cameraPositionState: CameraPositionState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var latLong by remember { mutableStateOf<LatLng?>(null) }
    var zoom by remember { mutableFloatStateOf(11f) }

    LaunchedEffect(Unit) {
        val location = getCurrentLocationSuspend(context)
        if (location != null) {
            zoom = 12f
            latLong = location
        } else {
            zoom = 11f
            val ipData = DataStorageManager.ipDB.firstOrNull()
            latLong = if (ipData != null) {
                LatLng(ipData.lat ?: 0.0, ipData.lon ?: 0.0)
            } else {
                LatLng(0.0, 0.0)
            }
        }
    }


    if (latLong != null) {
        val clusterItems = remember(events) { events.map { EventClusterItem(it) } }

        var selectedEvents by remember { mutableStateOf<List<EventsResponsesItems>>(emptyList()) }
        var showSheet by remember { mutableStateOf(false) }

        var cameraReady by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            scope.launch {
                delay(900)
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLong!!, zoom), 800)
                cameraReady = true
            }
        }

        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false,
                    compassEnabled = false,
                    mapToolbarEnabled = false
                )
            ) {
                val clusterManager = rememberClusterManager<EventClusterItem>()

                MapEffect(clusterManager) { map ->
                    try {
                        map.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.dark_map_style
                            )
                        )
                    } catch (_: Exception) {
                    }

                    if (clusterManager != null) {
                        clusterManager.algorithm =
                            NonHierarchicalDistanceBasedAlgorithm<EventClusterItem>().apply {
                                maxDistanceBetweenClusteredItems = 100
                            }

                        clusterManager.renderer = object : DefaultClusterRenderer<EventClusterItem>(
                            context, map, clusterManager
                        ) {
                            override fun onClusterItemRendered(
                                clusterItem: EventClusterItem, marker: Marker
                            ) {
                                super.onClusterItemRendered(clusterItem, marker)
                                scope.launch {
                                    val icon = getCircularMarkerBitmap(
                                        context, clusterItem.event.thumbnail
                                    )
                                    marker.setIcon(icon)
                                }
                            }

                            override fun shouldRenderAsCluster(cluster: Cluster<EventClusterItem>): Boolean {
                                return cluster.size > 1
                            }
                        }

                        clusterManager.setOnClusterClickListener { cluster ->
                            selectedEvents = cluster.items.map { it.event }
                            showSheet = true
                            true
                        }

                        clusterManager.setOnClusterItemClickListener { item ->
                            selectedEvents = listOf(item.event)
                            showSheet = true
                            true
                        }
                    }
                }
                if (clusterManager != null && cameraReady) {
                    Clustering(clusterItems, clusterManager)
                }
            }

            if (showSheet) {
                EventListOverlay(selectedEvents, { showSheet = false })
            }
        }
    }
}

data class EventClusterItem(val event: EventsResponsesItems) : ClusterItem {
    override fun getPosition() = LatLng(event.geo?.lat ?: 0.0, event.geo?.lng ?: 0.0)

    override fun getTitle(): String? = event.name
    override fun getSnippet(): String? = event.address
    override fun getZIndex(): Float? = null
}