package com.rizwansayyed.zene.presenter.ui.extra.standby.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.PurpleGrey80
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextPorkys
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.HOUR_12
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.HOUR_24
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.MONTH_DAY_TIME
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.SIMPLE_MINUTES
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.SIMPLE_SECONDS
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.SIMPLE_TIME
import com.rizwansayyed.zene.utils.DateFormatter.toDate
import com.rizwansayyed.zene.utils.Utils.is24Hour
import com.rizwansayyed.zene.utils.Utils.isPermission
import com.rizwansayyed.zene.utils.Utils.phoneBatteryLevel
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun StandbyViewTime() {
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()

    var job by remember { mutableStateOf<Job?>(null) }
    var isReadCalenderPermission by remember { mutableStateOf(false) }

    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            isReadCalenderPermission = isPermission(Manifest.permission.READ_CALENDAR)
            if (it) homeApiViewModel.getTodayEvents()
        }

    var hourFirst by remember { mutableIntStateOf(0) }
    var hourSecond by remember { mutableIntStateOf(0) }
    var minFirst by remember { mutableIntStateOf(0) }
    var minSecond by remember { mutableIntStateOf(0) }
    var secFirst by remember { mutableIntStateOf(0) }
    var secSecond by remember { mutableIntStateOf(0) }
    var date by remember { mutableStateOf("") }

    var batteryLevel by remember { mutableIntStateOf(0) }

    Spacer(Modifier.height(30.dp))

    Row(Modifier.padding(start = 14.dp), Arrangement.Center, Alignment.CenterVertically) {
        Image(painterResource(R.mipmap.logo), "", Modifier.size(20.dp))
        Spacer(Modifier.width(5.dp))
        TextThin(v = stringResource(R.string.app_name))
    }

    Row(Modifier.padding(start = 14.dp), Arrangement.Center, Alignment.CenterVertically) {
        TimeSliderAnimation(hourFirst, 120)
        TimeSliderAnimation(hourSecond, 120)

        Spacer(Modifier.height(15.dp))
        TextPorkys(v = ":", 40)
        Spacer(Modifier.height(15.dp))

        TimeSliderAnimation(minFirst, 120)
        TimeSliderAnimation(minSecond, 120)

        Row(Modifier.padding(top = 35.dp, start = 5.dp)) {
            TimeSliderAnimation(secFirst, 30, MainColor)
            TimeSliderAnimation(secSecond, 30, MainColor)
        }
    }

    Row(Modifier.padding(start = 16.dp), Arrangement.Center, Alignment.CenterVertically) {
        TextBold(v = date, size = 20)

        Spacer(Modifier.width(15.dp))

        BatteryStatusLevel(batteryLevel)
    }

    if (isReadCalenderPermission)
        if (homeApiViewModel.calenderTodayEvents.isEmpty()) Column(
            Modifier
                .padding(start = 16.dp, top = 30.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MainColor)
                .padding(10.dp)
        ) {
            TextThin(v = stringResource(R.string.no_events_today))
        }
        else Column(Modifier.padding(top = 20.dp)) {
            TextThin(v = stringResource(R.string.today_events), Modifier.padding(start = 16.dp))

            LazyRow(Modifier.fillMaxWidth()) {
                item {
                    Spacer(Modifier.width(15.dp))
                }
                items(homeApiViewModel.calenderTodayEvents) {
                    Column(
                        Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MainColor)
                            .padding(10.dp)
                    ) {
                        TextThin(
                            v = "${toDate(SIMPLE_TIME, it.startTime)} - " +
                                    toDate(SIMPLE_TIME, it.endTime), size = 12
                        )
                        Spacer(Modifier.height(15.dp))

                        TextSemiBold(
                            v = if ((it.title?.length ?: 0) > 25)
                                "${it.title?.substring(0, 25)}..." else it.title ?: "", size = 14
                        )

                        Spacer(Modifier.height(15.dp))
                    }
                }
                item {
                    Spacer(Modifier.width(25.dp))
                }
            }
        }
    else
        TextThin(
            v = stringResource(R.string.need_permission_to_show_events_from_calender),
            Modifier
                .padding(start = 50.dp, top = 40.dp)
                .clickable {
                    permission.launch(Manifest.permission.READ_CALENDAR)
                }, true
        )



    DisposableEffect(Unit) {
        homeApiViewModel.getTodayEvents()
        isReadCalenderPermission = isPermission(Manifest.permission.READ_CALENDAR)

        job = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                hourFirst = if (is24Hour()) toDate(HOUR_24).toCharArray()[0].digitToInt()
                else toDate(HOUR_12).toCharArray()[0].digitToInt()

                hourSecond = if (is24Hour()) toDate(HOUR_24).toCharArray()[1].digitToInt()
                else toDate(HOUR_12).toCharArray()[1].digitToInt()

                minFirst = toDate(SIMPLE_MINUTES).toCharArray()[0].digitToInt()
                minSecond = toDate(SIMPLE_MINUTES).toCharArray()[1].digitToInt()

                secFirst = toDate(SIMPLE_SECONDS).toCharArray()[0].digitToInt()
                secSecond = toDate(SIMPLE_SECONDS).toCharArray()[1].digitToInt()

                date = toDate(MONTH_DAY_TIME)

                batteryLevel = phoneBatteryLevel()

                delay(1.seconds)
            }
        }
        onDispose {
            job?.cancel()
        }
    }
}

@Composable
fun BatteryStatusLevel(batteryLevel: Int) {
    Box {
        CircularProgressIndicator(
            progress = { (batteryLevel.toFloat() / 100) },
            modifier = Modifier
                .offset(y = 2.dp)
                .width(35.dp)
                .align(Alignment.Center),
            strokeWidth = 4.dp,
            color = if (batteryLevel > 15) PurpleGrey80 else Color.Red,
            trackColor = Color.Transparent
        )

        TextThin(
            v = "$batteryLevel%",
            Modifier.align(Alignment.Center),
            size = 12
        )
    }
}


@Composable
fun TimeSliderAnimation(v: Int, s: Int, color: Color = Color.White) {
    AnimatedContent(
        targetState = v,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInVertically { height -> height } + fadeIn(tween(500))).togetherWith(
                    slideOutVertically { height -> -height } + fadeOut(tween(500)))
            } else {
                (slideInVertically { height -> -height } + fadeIn(tween(500))).togetherWith(
                    slideOutVertically { height -> height } + fadeOut(tween(500)))
            }.using(
                SizeTransform(clip = false)
            )
        }, label = "", contentAlignment = Alignment.Center
    ) { targetCount ->
        TextPorkys(v = targetCount.toString(), s, color)
    }
}