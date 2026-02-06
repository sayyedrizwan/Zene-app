package com.rizwansayyed.zene.ui.main.ent.nav

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.EventInfoResponse
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.ui.main.ent.EntertainmentViewModel
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.LoveBuzzBg
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.openGoogleMapLocation
import com.rizwansayyed.zene.utils.addEventToCalendar
import com.rizwansayyed.zene.utils.getEventTimeToMS
import com.rizwansayyed.zene.utils.share.MediaContentUtils.openCustomBrowser
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia

@Composable
fun EventNavLoading() {
    Column(Modifier.fillMaxSize()) {
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            repeat(2) {
                ShimmerEffect(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(45.dp))

        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .width(160.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(12.dp))

        ShimmerEffect(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(12.dp))


        Spacer(modifier = Modifier.height(224.dp))
    }
}

@Composable
fun EventNavView(id: String) {
    val viewModel: EntertainmentViewModel = hiltViewModel()
    val scrollState = rememberScrollState()

    val context = LocalContext.current.applicationContext

    Box(
        Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        when (val v = viewModel.eventsFullInfo) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> EventNavLoading()
            is ResponseResult.Success -> {
                if (v.data.artistName == null && v.data.ticketUrl == null) {
                    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                        TextViewBold(stringResource(R.string.no_event_found), 25)
                    }
                } else Column(
                    Modifier
                        .background(Color.Black)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    EventHeaderSection(v.data, id)

                    EventInfoCard(v.data)

                    OutlinedButton(
                        onClick = {
                            val startMS = getEventTimeToMS(v.data.date)
                            val endMS = getEventTimeToMS(v.data.endDate)

                            val location = if (v.data.venue.orEmpty()
                                    .trim().length > 3
                            ) "${v.data.venue}, ${v.data.city}, ${v.data.state}"
                            else "${v.data.city}, ${v.data.state}"

                            addEventToCalendar(
                                context, "${v.data.artistName} Concert", location, startMS, endMS
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 45.dp)
                            .padding(horizontal = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.DarkGray),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        ImageIcon(R.drawable.ic_calendar, 19)
                        Spacer(Modifier.width(8.dp))
                        TextViewSemiBold(stringResource(R.string.add_to_calendar), 15)
                    }

                    val location =
                        remember { LatLng(v.data.latitude ?: 0.0, v.data.longitude ?: 0.0) }
                    val markerState = rememberUpdatedMarkerState(position = location)

                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(location, 13f)
                    }

                    val mapStyleOptions = remember {
                        MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_map_style)
                    }


                    GoogleMap(
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .height(300.dp)
                            .fillMaxWidth(),
                        onMapClick = {
                            openGoogleMapLocation(
                                false,
                                location.latitude,
                                location.longitude,
                                v.data.artistName ?: "Event"
                            )
                        },
                        cameraPositionState = cameraPositionState,
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            zoomGesturesEnabled = false,
                            scrollGesturesEnabled = false,
                            tiltGesturesEnabled = false,
                            compassEnabled = false,
                            myLocationButtonEnabled = false,
                            mapToolbarEnabled = false
                        ),
                        properties = MapProperties(
                            isMyLocationEnabled = false, mapStyleOptions = mapStyleOptions
                        )
                    ) {
                        MarkerComposable(state = markerState) {
                            Box(
                                Modifier
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(100))
                                    .background(Color.Black)
                                    .padding(5.dp)
                            ) {
                                ImageIcon(R.drawable.ic_location, 25)
                            }
                        }
                    }

                    if (v.data.upcomingConcerts?.isNotEmpty() == true) {
                        Spacer(Modifier.height(90.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.upcoming_concerts), 23)
                        }
                        Spacer(Modifier.height(15.dp))

                        v.data.upcomingConcerts.forEach { item ->
                            UpcomingConcertCard(item, v.data.eventImage)
                        }
                    }

                    Spacer(Modifier.height(224.dp))
                }

                Button(
                    onClick = {
                        openCustomBrowser(v.data.ticketUrl)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LoveBuzzBg
                    )
                ) {
                    TextViewBold(stringResource(R.string.get_tickets_now))
                }
            }
        }

        ButtonArrowBack(Modifier.align(Alignment.TopStart))
    }
    LaunchedEffect(Unit) {
        if (viewModel.eventsFullInfo !is ResponseResult.Success) {
            viewModel.eventFullInfo(id)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EventHeaderSection(data: EventInfoResponse, id: String) {
    Box {
        GlideImage(
            data.eventImage, data.artistName,
            Modifier
                .fillMaxWidth()
                .height(500.dp),
            contentScale = ContentScale.Crop,
        )

        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(500.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.35f),
                            Color.Black.copy(alpha = 0.65f),
                            Color.Black.copy(alpha = 0.85f),
                            Color.Black.copy(alpha = 0.95f),
                        )
                    )
                )
        )


        Row(
            Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                if (data.happeningIn.orEmpty()
                        .trim().length > 3
                ) TextViewLight(data.happeningIn.orEmpty(), 16, Color.Red)

                TextViewBold(data.artistName.orEmpty(), 25)

                if (data.venue.orEmpty()
                        .trim().length > 3
                ) TextViewSemiBold("${data.venue}, ${data.city}, ${data.state}", 14)
                else TextViewSemiBold("${data.city}, ${data.state}", 14)
            }

            Box(
                Modifier
                    .padding(horizontal = 7.dp)
                    .clickable {
                        NavigationUtils.triggerInfoSheet(data.toMusicData(id))
                    }) {
                ImageIcon(R.drawable.ic_share, 24)
            }
        }
    }
}

@Composable
fun EventInfoCard(data: EventInfoResponse) {
    val (date, time) = data.formatDateTime()

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        InfoChip(
            R.drawable.ic_calendar, R.string.date, date, Modifier.weight(1f)
        )

        InfoChip(
            R.drawable.ic_time, R.string.starts_at, time, Modifier.weight(1f)
        )
    }
}

@Composable
fun InfoChip(icon: Int, title: Int, value: String, modifier: Modifier) {
    Column(
        modifier
            .padding(10.dp)
            .background(MainColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        ImageIcon(icon, 22)
        Spacer(Modifier.height(10.dp))
        TextViewLight(stringResource(title).uppercase(), 11)
        Spacer(Modifier.height(5.dp))
        TextViewBold(value, 19)
    }
}


@Composable
fun UpcomingConcertCard(v: EventInfoResponse.UpcomingConcerts, eventImage: String?) {
    Row(
        Modifier
            .padding(bottom = 15.dp)
            .padding(horizontal = 15.dp)
            .fillMaxSize()
            .combinedClickable(onLongClick = {
                NavigationUtils.triggerInfoSheet(v.toMusicData(eventImage))
            }, onClick = {
                startMedia(v.toMusicData(eventImage))
            })
            .background(MainColor, RoundedCornerShape(16.dp))
            .padding(12.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            Modifier.size(69.dp), RoundedCornerShape(12.dp),
            CardDefaults.cardColors(containerColor = DarkCharcoal, contentColor = DarkCharcoal),
            elevation = CardDefaults.cardElevation(6.dp),
        ) {
            Column(
                Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally
            ) {
                TextViewSemiBold(v.month.orEmpty(), 13, Color.Red)
                TextViewSemiBold(v.day.orEmpty(), 22, Color.White)
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            TextViewLight(v.date.orEmpty(), 12, Color.White)
            Spacer(Modifier.height(4.dp))
            TextViewSemiBold(v.title.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))
            TextViewLight("${v.venue.orEmpty()}, ${v.location.orEmpty()}", 12, Color.White)
        }
    }
}