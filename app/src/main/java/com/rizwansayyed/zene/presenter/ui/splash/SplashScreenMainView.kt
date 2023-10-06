package com.rizwansayyed.zene.presenter.ui.splash

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.presenter.theme.MainColor
import kotlinx.coroutines.delay
import kotlin.streams.toList
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


@Composable
fun SplashScreenMain() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MainColor)
    ) {

        var textToDisplay by remember { mutableStateOf("") }
        var change by remember { mutableStateOf(false) }

        val a by animateAlignmentAsState(if (change) Alignment.TopCenter else Alignment.BottomCenter)


        LaunchedEffect(Unit) {
            textsAppNameList.forEach {
                textToDisplay += it
                delay(400.milliseconds)
            }

            change = true
        }

        Text(
            text = textToDisplay,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(a).animateContentSize()
        )
    }
}

val textsAppNameList = listOf("Z", "E", "N", "E")


@SuppressLint("UnrememberedMutableState")
@Composable
fun animateAlignmentAsState(
    targetAlignment: Alignment,
): State<Alignment> {
    val biased = targetAlignment as BiasAlignment
    val horizontal by animateFloatAsState(biased.horizontalBias, label = "")
    val vertical by animateFloatAsState(biased.verticalBias, label = "")
    return derivedStateOf { BiasAlignment(horizontal, vertical) }
}
