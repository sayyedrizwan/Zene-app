package com.rizwansayyed.zene.ui.main.ent

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.EventsResponsesItems
import com.rizwansayyed.zene.ui.main.ent.view.EventAllEventsLists
import com.rizwansayyed.zene.ui.main.ent.view.EventsMapScreen
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

suspend fun getCircularMarkerBitmap(context: Context, url: String?): BitmapDescriptor {
    return withContext(Dispatchers.IO) {
        try {
            val bitmap = Glide.with(context).asBitmap().load(url).centerCrop().submit(80, 80).get()
            val output = createBitmap(bitmap.width, bitmap.height)
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

@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun EntertainmentEventsView(viewModel: EntertainmentViewModel) {
    val cameraPositionState = rememberCameraPositionState()

    when (val v = viewModel.discover) {
        ResponseResult.Empty -> {}
        ResponseResult.Loading -> {}
        is ResponseResult.Error -> {}

        is ResponseResult.Success -> {
            val allEvents =
                (v.data.events?.thisWeek.orEmpty() + v.data.events?.city.orEmpty() + v.data.events?.all.orEmpty()).distinctBy { it.id }

            val sheetState = rememberStandardBottomSheetState(
                SheetValue.PartiallyExpanded, skipHiddenState = true
            )
            val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

            Box(Modifier.fillMaxSize()) {
                EventsMapScreen(allEvents, cameraPositionState)

                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    containerColor = Color.Transparent,
                    sheetPeekHeight = 250.dp,
                    sheetContainerColor = MainColor,
                    sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    sheetContent = {
                        EventAllEventsLists(v.data, scaffoldState, cameraPositionState)
//                        LazyColumn {
//                            items(allEvents) { event ->
//                                EventItemRow(event) {
//                                    scope.launch {
//                                        scaffoldState.bottomSheetState.partialExpand()
//                                        val latLong =
//                                            LatLng(event.geo?.lat ?: 0.0, event.geo?.lng ?: 0.0)
//                                        cameraPositionState.animate(
//                                            CameraUpdateFactory.newLatLngZoom(
//                                                latLong,
//                                                16f
//                                            ), 800
//                                        )
//                                    }
//                                }
//                            }
//
//
//                            item { Spacer(Modifier.height(32.dp)) }
//                        }
                    }) { }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListOverlay(events: List<EventsResponsesItems>, onClose: () -> Unit) {
    ModalBottomSheet(
        onClose, sheetState = rememberModalBottomSheetState(), containerColor = MainColor,
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .background(MainColor),
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
fun EventItemRow(event: EventsResponsesItems, click: () -> Unit = {}) {
    Row(
        Modifier
            .padding(8.dp)
            .clickable {
                click()
            }
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

