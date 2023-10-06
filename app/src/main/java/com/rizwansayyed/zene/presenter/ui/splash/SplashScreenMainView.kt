package com.rizwansayyed.zene.presenter.ui.splash

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextAntroSemiBold
import com.rizwansayyed.zene.presenter.ui.TextLight
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun SplashScreenMain(done: () -> Unit) {
    val appName = stringResource(R.string.app_name)
    var name by remember { mutableStateOf("") }
    var showFounderName by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(MainColor)
    ) {
        TextAntroSemiBold(
            name,
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .animateContentSize()
                .padding(vertical = 50.dp),
            color = Color.White,
            singleLine = true,
            doCenter = true,
            size = 85
        )

        AnimatedVisibility(
            showFounderName,
            Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 50.dp)
                .fillMaxWidth()
        ) {
            TextLight(
                stringResource(R.string.by_rs),
                Modifier.fillMaxWidth(),
                true, Color.Gray, true, 13
            )
        }

        LaunchedEffect(Unit) {
            appName.split("").forEach {
                name += it
                delay(350.milliseconds)
            }

            showFounderName = true
            delay(2.seconds)
            done()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardButton(v: String, color: Color, colorT: Color, modifier: Modifier, click: () -> Unit) {
    Card(
        { click() },
        modifier.fillMaxWidth(), colors = CardDefaults.cardColors(color)
    ) {
        TextSemiBold(v, Modifier.padding(16.dp).fillMaxWidth(), true, colorT)
    }
}