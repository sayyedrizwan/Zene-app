package com.rizwansayyed.zene.ui.main.ent

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.EventsResponsesItems
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

suspend fun getCircularMarkerBitmap(context: Context, url: String?): BitmapDescriptor {
    return withContext(Dispatchers.IO) {
        try {
            val bitmap = Glide.with(context).asBitmap().load(url).centerCrop().submit(80, 80).get()
            val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint().apply {
                isAntiAlias = true
            }
            canvas.drawCircle(bitmap.width / 2f, bitmap.height / 2f, bitmap.width / 2f, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            val border = Paint().apply {
                style = Paint.Style.STROKE; strokeWidth = 4f; color =
                Color.White.toArgb(); isAntiAlias = true
            }

            canvas.drawCircle(
                bitmap.width / 2f, bitmap.height / 2f, (bitmap.width / 2f) - 2f, border
            )
            BitmapDescriptorFactory.fromBitmap(output)
        } catch (_: Exception) {
            BitmapDescriptorFactory.defaultMarker()
        }
    }
}

@MapsComposeExperimentalApi
@Composable
fun EntertainmentEventsView(viewModel: EntertainmentViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        when (val v = viewModel.discover) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {}
            is ResponseResult.Success -> {
                val allEvents =
                    (v.data.events?.thisWeek.orEmpty() + v.data.events?.city.orEmpty() + v.data.events?.all.orEmpty()).distinctBy { it.id }
                EventsMapScreen(allEvents)
            }
        }
    }
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventsMapScreen(events: List<EventsResponsesItems>) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
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
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLong!!, zoom)
        }

        Box(
            Modifier
                .fillMaxWidth()
                .height(screenHeightDp * 0.6f)
        ) {
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
                if (clusterManager != null) {
                    Clustering(clusterItems, clusterManager)
                }
            }
            if (showSheet) {
                EventListOverlay(selectedEvents, { showSheet = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListOverlay(events: List<EventsResponsesItems>, onClose: () -> Unit) {
    ModalBottomSheet(
        onClose,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.Black,
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .background(Color.Black),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(events) { event -> EventItemRow(event) }

            item {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EventItemRow(event: EventsResponsesItems) {
    Row(
        Modifier
            .padding(8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            event.thumbnail, null, modifier = Modifier
                .size(100.dp)
                .padding(4.dp)
        )

        Column(
            Modifier
                .padding(start = 8.dp)
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            TextViewBold(event.name ?: "Unnamed Event", 16)
            TextViewNormal(event.address ?: "Unnamed Event", 14, line = 1)
            TextViewNormal(event.dateWithTime ?: "Unnamed Event", 14, line = 1)
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocationSuspend(context: Context): LatLng? =
    suspendCancellableCoroutine { cont ->
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }

        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        fusedClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                cont.resume(LatLng(location.latitude, location.longitude))
            } else {
                cont.resume(null)
            }
        }.addOnFailureListener { cont.resume(null) }
    }

data class EventClusterItem(
    val event: EventsResponsesItems
) : ClusterItem {

    override fun getPosition(): LatLng = LatLng(event.geo?.lat ?: 0.0, event.geo?.lng ?: 0.0)

    override fun getTitle(): String? = event.name
    override fun getSnippet(): String? = event.address
    override fun getZIndex(): Float? = null
}