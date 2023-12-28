package com.rizwansayyed.zene.presenter.ui.extra.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextPorkys
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.HOUR_12
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.HOUR_24
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.MONTH_DAY_TIME
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.SIMPLE_MINUTES
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.SIMPLE_SECONDS
import com.rizwansayyed.zene.utils.DateFormatter.toDate
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.is24Hour
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun StandbyViewTime() {
    var job by remember { mutableStateOf<Job?>(null) }

    var hourFirst by remember { mutableIntStateOf(0) }
    var hourSecond by remember { mutableIntStateOf(0) }
    var minFirst by remember { mutableIntStateOf(0) }
    var minSecond by remember { mutableIntStateOf(0) }
    var secFirst by remember { mutableIntStateOf(0) }
    var secSecond by remember { mutableIntStateOf(0) }
    var date by remember { mutableStateOf("") }

    Spacer(Modifier.height(30.dp))

    Row(Modifier.padding(start = 14.dp), Arrangement.Center, Alignment.CenterVertically) {
        TimeSliderAnimation(hourFirst, 120)
        TimeSliderAnimation(hourSecond, 120)

        Spacer(Modifier.height(15.dp))
        TextPorkys(v = ":", 40)
        Spacer(Modifier.height(15.dp))

        TimeSliderAnimation(minFirst, 120)
        TimeSliderAnimation(minSecond, 120)

        Row(Modifier.padding(top = 35.dp, start = 5.dp)) {
            TimeSliderAnimation(secFirst, 30)
            TimeSliderAnimation(secSecond, 30)
        }
    }

    Spacer(Modifier.height(30.dp))

    Row(Modifier.padding(start = 16.dp), Arrangement.Center, Alignment.CenterVertically) {
        TextBold(v = date, size = 20)
    }

    DisposableEffect(Unit) {
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

                delay(1.seconds)
            }
        }
        onDispose {
            job?.cancel()
        }
    }
}


@Composable
fun TimeSliderAnimation(v: Int, s: Int) {
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
        TextPorkys(v = targetCount.toString(), s)
    }
}