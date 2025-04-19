package com.rizwansayyed.zene.ui.main.lux

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.lux.billing.BillingManager
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LuxView() {
    val context = LocalActivity.current
    var showPremium by remember { mutableStateOf(false) }
    var showBlastFirst by remember { mutableStateOf(false) }

    val homeViewModel: HomeViewModel = hiltViewModel()

    fun updatePurchase(token: String) {
        showPremium = true
        homeViewModel.updateSubscriptionToken(token) {
            ProcessPhoenix.triggerRebirth(context)
        }
    }

    val manager by remember { mutableStateOf(BillingManager(context!!, { updatePurchase(it) })) }

    val party = remember {
        Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
    }

    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column(
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(20.dp))
            TextViewBoldBig(stringResource(R.string.app_name), 120)
            TextViewBoldBig(stringResource(R.string.lux), 120)
            Spacer(Modifier.height(10.dp))
            Column(Modifier.padding(horizontal = 10.dp)) {
                TextViewNormal(stringResource(R.string.lux_tag_line), 18)
            }

            Spacer(Modifier.height(35.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .clickable { manager.buyYearly() }
                    .padding(vertical = 10.dp, horizontal = 25.dp),
                Arrangement.Center,
                Alignment.CenterVertically) {
                ImageIcon(R.drawable.ic_crown, 21, Color.Black)
                Spacer(Modifier.width(10.dp))
                TextViewSemiBold(
                    stringResource(R.string.start_free_trial), 17, Color.Black
                )
            }

            Spacer(Modifier.height(40.dp))
            TextViewNormal(
                stringResource(R.string.lux_only_for_music_enthusiasts), 15, center = true
            )
            Spacer(Modifier.height(20.dp))
            TextViewNormal(stringResource(R.string.upgrading_lux_give_ad_free), 15, center = true)

            Spacer(Modifier.height(80.dp))
            BillingPeriod.entries.forEach {
                LuxItemView(manager, it)
                Spacer(Modifier.height(20.dp))
            }

            TextViewLight(
                stringResource(R.string.you_can_cancel_subscription_anytime), 15, center = true
            )

            Spacer(Modifier.height(400.dp))
        }

        AnimatedVisibility(showPremium) {
            Box(
                Modifier
                    .pointerInput(Unit) { detectDragGestures { _, _ -> } }
                    .fillMaxSize()
                    .background(BlackTransparent), Alignment.Center) {

                KonfettiView(Modifier.fillMaxSize(), listOf(party, party, party))

                if (showBlastFirst)
                    KonfettiView(Modifier.fillMaxSize(), listOf(party, party, party))

                Column(
                    Modifier
                        .fillMaxWidth()
                        .offset(y = (-100).dp)
                ) {

                    GlideImage(
                        R.raw.crown_animtaion, stringResource(R.string.lux), Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    TextViewNormal(
                        stringResource(R.string.you_re_a_premium_your_now), 15, center = true
                    )

                }
                LaunchedEffect(Unit) {
                    delay(1.seconds)
                    showBlastFirst = true
                }
            }
        }

        LaunchedEffect(Unit) {
            manager.startConnection()
        }
    }
}
