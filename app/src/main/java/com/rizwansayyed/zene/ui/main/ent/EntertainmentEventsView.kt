package com.rizwansayyed.zene.ui.main.ent

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.EventsResponsesItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@MapsComposeExperimentalApi
@Composable
fun EntertainmentEventsView(viewModel: EntertainmentViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = viewModel.discover) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {}
            is ResponseResult.Success -> {
//                val fullEvents = (v.data.events?.city.orEmpty()) +
//                        (v.data.events?.thisWeek.orEmpty()) + (v.data.events?.all.orEmpty())

                item {
//                    val context = LocalContext.current
//                    val clusterItems by viewModel.clusterItems.collectAsState()
//                    var selectedEvents by remember {
//                        mutableStateOf<List<EventsResponsesItems>>(emptyList())
//                    }
//
//                    LaunchedEffect(fullEvents) {
//                        viewModel.loadMapsEvents(context, fullEvents)
//                    }

//                    GoogleMap(
//                        modifier = Modifier.fillMaxSize(),
//                        cameraPositionState = rememberCameraPositionState()
//                    ) {
//                        MapEffect(clusterItems) { googleMap ->
//                            val clusterManager =
//                                ClusterManager<EventsResponsesItems>(context, googleMap)
//
//                            clusterManager.renderer =
//                                EventClusterRenderer(context, googleMap, clusterManager)
//
//                            googleMap.setOnCameraIdleListener(clusterManager)
//                            googleMap.setOnMarkerClickListener(clusterManager)
//
//                            clusterManager.setOnClusterClickListener {
//                                selectedEvents = it.items.toList()
//                                true
//                            }
//
//                            clusterManager.setOnClusterItemClickListener {
//                                selectedEvents = listOf(it)
//                                true
//                            }
//
//                            clusterManager.clearItems()
//                            clusterManager.addItems(clusterItems)
//                            clusterManager.cluster()
//                        }
//                    }

//                    if (selectedEvents.isNotEmpty()) {
//                        EventsBottomSheet(
//                            events = selectedEvents,
//                            onDismiss = { selectedEvents = emptyList() }
//                        )
//                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EventsBottomSheet(events: List<EventsResponsesItems>, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn {
            items(events) { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    GlideImage(
                        model = event.thumbnail,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(Modifier.width(12.dp))
//                    Text(text = event.title.orEmpty())
                }
            }
        }
    }
}


suspend fun loadBitmapFromUrl(
    context: Context,
    url: String,
    sizePx: Int = 128
): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(sizePx, sizePx)
                .get()
        } catch (_: Exception) {
            null
        }
    }
}

//class EventClusterRenderer(
//    context: Context,
//    map: GoogleMap,
//    clusterManager: ClusterManager<EventsResponsesItems>
//) : DefaultClusterRenderer<EventsResponsesItems>(context, map, clusterManager) {
//
//
//    override fun onBeforeClusterItemRendered(
//        item: EventsResponsesItems,
//        markerOptions: MarkerOptions
//    ) {
//        item.thumbnailBitmap?.let { bitmap ->
//            markerOptions.icon(
//                BitmapDescriptorFactory.fromBitmap(
//                    Bitmap.createScaledBitmap(bitmap, 96, 96, true)
//                )
//            )
//        }
//        markerOptions.title(item.title)
//    }
//
//    override fun onBeforeClusterRendered(
//        cluster: Cluster<EventsResponsesItems>,
//        markerOptions: MarkerOptions
//    ) {
//        val bitmap = createClusterBitmap(
//            cluster.items.mapNotNull { it.thumbnailBitmap }
//        )
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
//    }
//
//    private fun createClusterBitmap(bitmaps: List<Bitmap>): Bitmap {
//        val size = 140
//        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bmp)
//
//        val cell = size / 2
//        bitmaps.take(4).forEachIndexed { index, bitmap ->
//            val left = (index % 2) * cell
//            val top = (index / 2) * cell
//            canvas.drawBitmap(
//                Bitmap.createScaledBitmap(bitmap, cell, cell, true),
//                left.toFloat(),
//                top.toFloat(),
//                null
//            )
//        }
//        return bmp
//    }
//}

