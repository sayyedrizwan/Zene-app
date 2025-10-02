package com.rizwansayyed.zene.ui.main.lux

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.lux.billing.BillingManager
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.FacebookColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PRIVACY_POLICY_URL
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PRIVACY_TERMS_URL
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LuxView() {
    val context = LocalActivity.current
    val homeViewModel: HomeViewModel = hiltViewModel()

    fun updatePurchase(token: String, subscriptionId: String) {
        homeViewModel.updateSubscriptionToken(token, subscriptionId) {
            ProcessPhoenix.triggerRebirth(context)
        }
//        showPremium = true
    }


    val manager by remember {
        mutableStateOf(BillingManager(context!!, { v, v1 -> updatePurchase(v, v1) }))
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GlideImage(
            R.raw.lux_bg,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(top = 340.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(DarkCharcoal)
                    .padding(horizontal = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                TextViewSemiBold(stringResource(R.string.app_name_lux), size = 45, center = true)
                Spacer(modifier = Modifier.height(8.dp))
                TextViewNormal(stringResource(R.string.lux_tag_line), size = 15, center = true)
                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .clickable {
                            manager.buyYearly()
                        }
                        .padding(vertical = 10.dp, horizontal = 25.dp),
                    Arrangement.Center,
                    Alignment.CenterVertically) {
                    ImageIcon(R.drawable.ic_crown, 21, Color.Black)
                    Spacer(Modifier.width(10.dp))
                    TextViewSemiBold(
                        stringResource(R.string.start_free_trial), 17, Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))


                LuxTypeChipsView()

                Spacer(modifier = Modifier.height(20.dp))

                LuxItemView(manager)
//                BillingPeriod.entries.forEach {
//                    LuxItemView(manager, it)
//                    Spacer(Modifier.height(20.dp))
//                }

                Spacer(modifier = Modifier.height(40.dp))

                LuxUsersReview()
                Spacer(modifier = Modifier.height(60.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                    Row(Modifier.clickable {
                        MediaContentUtils.openCustomBrowser(ZENE_PRIVACY_TERMS_URL)
                    }) {
                        TextViewNormal(
                            stringResource(R.string.terms_of_service),
                            size = 14,
                            color = FacebookColor
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    TextViewNormal("&", size = 14)
                    Spacer(modifier = Modifier.width(10.dp))
                    Row(Modifier.clickable {
                        MediaContentUtils.openCustomBrowser(ZENE_PRIVACY_POLICY_URL)
                    }) {
                        TextViewNormal(
                            stringResource(R.string.privacy_policy),
                            size = 14,
                            color = FacebookColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                TextViewNormal(
                    stringResource(R.string.lux_subscription_footer),
                    size = 13,
                    center = true,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(300.dp))
            }
        }
    }
}


//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun LuxView() {
//    val context = LocalActivity.current
//    var showPremium by remember { mutableStateOf(false) }
//    var showBlastFirst by remember { mutableStateOf(false) }
//
//    val homeViewModel: HomeViewModel = hiltViewModel()
//
//    fun updatePurchase(token: String, subscriptionId: String) {
//        homeViewModel.updateSubscriptionToken(token, subscriptionId) {
//            ProcessPhoenix.triggerRebirth(context)
//        }
//        showPremium = true
//    }
//
//    val manager by remember {
//        mutableStateOf(BillingManager(context!!, { v, v1 -> updatePurchase(v, v1) }))
//    }
//
//    val party = remember {
//        Party(
//            speed = 0f,
//            maxSpeed = 30f,
//            damping = 0.9f,
//            spread = 360,
//            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
//            position = Position.Relative(0.5, 0.3),
//            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
//        )
//    }
//
//    Box(Modifier.fillMaxSize(), Alignment.Center) {
//        Column(
//            Modifier
//                .padding(horizontal = 10.dp)
//                .fillMaxWidth()
//                .verticalScroll(rememberScrollState())
//        ) {
//            Spacer(Modifier.height(20.dp))
//            TextViewBoldBig(stringResource(R.string.app_name), 120)
//            TextViewBoldBig(stringResource(R.string.lux), 120)
//            Spacer(Modifier.height(10.dp))
//            Column(Modifier.padding(horizontal = 10.dp)) {
//                TextViewNormal(stringResource(R.string.lux_tag_line), 18)
//            }
//
//            Spacer(Modifier.height(35.dp))
//            Row(
//                modifier = Modifier
//                    .padding(horizontal = 4.dp)
//                    .clip(RoundedCornerShape(18.dp))
//                    .background(Color.White)
//                    .clickable { manager.buyYearly() }
//                    .padding(vertical = 10.dp, horizontal = 25.dp),
//                Arrangement.Center,
//                Alignment.CenterVertically) {
//                ImageIcon(R.drawable.ic_crown, 21, Color.Black)
//                Spacer(Modifier.width(10.dp))
//                TextViewSemiBold(
//                    stringResource(R.string.start_free_trial), 17, Color.Black
//                )
//            }
//
//            Spacer(Modifier.height(40.dp))
//            TextViewNormal(
//                stringResource(R.string.lux_only_for_music_enthusiasts), 15, center = true
//            )
//            Spacer(Modifier.height(20.dp))
//            TextViewNormal(stringResource(R.string.upgrading_lux_give_ad_free), 15, center = true)
//
//            Spacer(Modifier.height(80.dp))
//
//            LuxUsersReview()
//
//            Spacer(Modifier.height(80.dp))
//            BillingPeriod.entries.forEach {
//                LuxItemView(manager, it)
//                Spacer(Modifier.height(20.dp))
//            }
//
//            TextViewLight(
//                stringResource(R.string.you_can_cancel_subscription_anytime), 15, center = true
//            )
//
//            Spacer(Modifier.height(80.dp))
//            LuxCouponView(homeViewModel) {
//                showPremium = true
//            }
//
//
//            Spacer(Modifier.height(400.dp))
//        }
//
//        AnimatedVisibility(showPremium) {
//            Box(
//                Modifier
//                    .pointerInput(Unit) { detectDragGestures { _, _ -> } }
//                    .fillMaxSize()
//                    .background(BlackTransparent), Alignment.Center) {
//
//                KonfettiView(Modifier.fillMaxSize(), listOf(party, party, party))
//
//                if (showBlastFirst)
//                    KonfettiView(Modifier.fillMaxSize(), listOf(party, party, party))
//
//                Column(
//                    Modifier
//                        .fillMaxWidth()
//                        .offset(y = (-100).dp)
//                ) {
//
//                    GlideImage(
//                        R.raw.crown_animtaion, stringResource(R.string.lux), Modifier
//                            .fillMaxWidth()
//                            .height(200.dp)
//                    )
//
//                    TextViewNormal(
//                        stringResource(R.string.you_re_a_premium_your_now), 15, center = true
//                    )
//
//                    Spacer(Modifier.height(10.dp))
//                    TextViewNormal(
//                        stringResource(R.string.please_wait_updating_subscription_details),
//                        15, center = true
//                    )
//
//                }
//                LaunchedEffect(Unit) {
//                    delay(1.seconds)
//                    showBlastFirst = true
//                }
//            }
//        }
//
//        LaunchedEffect(Unit) {
//            manager.startConnection()
//        }
//    }
//}
