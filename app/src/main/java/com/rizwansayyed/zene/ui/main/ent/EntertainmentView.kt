package com.rizwansayyed.zene.ui.main.ent

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.ui.main.ent.view.EntertainmentScreenTopView
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector.BUZZ
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector.DATING
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector.DISCOVER
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector.EVENTS
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector.LIFESTYLE
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector.MOVIES
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun EntertainmentView(viewModel: NavigationViewModel) {
    val entViewModel: EntertainmentViewModel = hiltViewModel()
    val context = LocalActivity.current

    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .weight(1.2f)
                .fillMaxWidth()
        ) {
            EntertainmentScreenTopView(viewModel)
        }

        Box(
            Modifier
                .weight(8.8f)
                .fillMaxWidth()
        ) {
            when (viewModel.entNavSection) {
                DISCOVER -> EntertainmentDiscoverView(entViewModel, viewModel)
                BUZZ -> EntertainmentBuzzView(entViewModel)
                DATING -> EntertainmentDatingView(entViewModel)
                MOVIES -> EntertainmentMoviesView(entViewModel)
                EVENTS -> {}
                LIFESTYLE -> EntLifestyleView(entViewModel)
            }

        }
    }

    LaunchedEffect(Unit) {
        entViewModel.entDiscoverNews {
            CoroutineScope(Dispatchers.IO).safeLaunch {
                delay(5.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }

        entViewModel.entDiscoverLifeStyle()
        context?.let { InterstitialAdsUtils(it) }
    }

    DisposableEffect(Unit) {
        entViewModel.startReadersCounter()
        onDispose {
            entViewModel.stopReadersCounter()
        }
    }
}