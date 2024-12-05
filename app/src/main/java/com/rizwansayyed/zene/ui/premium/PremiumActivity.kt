package com.rizwansayyed.zene.ui.premium

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.MainActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.isUserPremiumDB
import com.rizwansayyed.zene.ui.premium.view.PremiumBuyingCards
import com.rizwansayyed.zene.ui.premium.view.PremiumCouponView
import com.rizwansayyed.zene.ui.premium.view.UsersReviewView
import com.rizwansayyed.zene.ui.premium.viewmodel.PremiumViewModel
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class PremiumActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val premiumViewModel: PremiumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isPremiumNow by remember { mutableStateOf(false) }

            ZeneTheme {
                if (isPremiumNow) Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(horizontal = 6.dp)
                ) {
                    var time by remember { mutableIntStateOf(6) }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        Arrangement.Center, Alignment.CenterHorizontally
                    ) {
                        ImageIcon(R.drawable.ic_crown, 250, Color.Yellow)
                        Spacer(Modifier.height(30.dp))
                        TextPoppinsSemiBold(
                            stringResource(R.string.subscription_activated_successfully),
                            true, size = 16
                        )
                        Spacer(Modifier.height(20.dp))
                        TextPoppins("$time", true, size = 16)
                    }

                    KonfettiView(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center), parties = listOf(
                            Party(
                                speed = 0f,
                                maxSpeed = 30f,
                                damping = 0.9f,
                                spread = 360,
                                size = listOf(nl.dionsegijn.konfetti.core.models.Size.LARGE),
                                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                                position = Position.Relative(0.5, 0.3),
                                emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(100),
                            )
                        )
                    )

                    LaunchedEffect(Unit) {
                        while (time > 0) {
                            delay(1.seconds)
                            time -= 1

                            if (time == 0) openMainActivity()
                        }
                    }
                } else Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(horizontal = 6.dp)
                ) {
                    Spacer(Modifier.height(100.dp))
                    TextPoppinsSemiBold(stringResource(R.string.zene_pro), true, size = 40)
                    Spacer(Modifier.height(2.dp))
                    TextPoppins(stringResource(R.string.zene_pro_desc), true, size = 16)
                    Spacer(Modifier.height(10.dp))
                    UsersReviewView()
                    Spacer(Modifier.height(80.dp))
                    PremiumBuyingCards(premiumViewModel)

                    Spacer(Modifier.height(6.dp))
                    TextPoppins(
                        stringResource(R.string.upgrade_text_subscription_text), true, size = 14
                    )
                    Spacer(Modifier.height(55.dp))
                    PremiumCouponView(premiumViewModel)
                    Spacer(Modifier.height(220.dp))
                }


                LaunchedEffect(premiumViewModel.purchase) {
                    if ((premiumViewModel.purchase?.orderId?.length ?: 0) > 3) {
                        isPremiumNow = true
                        isUserPremiumDB = flowOf(true)
                        homeViewModel.updateSubscription(premiumViewModel.purchase)
                    }
                }
            }
        }
    }


    private fun openMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }

}