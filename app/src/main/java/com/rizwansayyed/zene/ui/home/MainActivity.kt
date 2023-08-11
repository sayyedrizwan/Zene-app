package com.rizwansayyed.zene.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizwansayyed.zene.NetworkCallbackStatus
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homeui.HomepageView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.windowManagerNoLimit
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity(), NetworkCallbackStatus {

    companion object {
        lateinit var networkCallbackStatus: NetworkCallbackStatus
    }

    private val homeNavViewModel: HomeNavViewModel by viewModels()
    private val songsViewModel: SongsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkCallbackStatus = this

        setContent {
            window.setFlags(windowManagerNoLimit, windowManagerNoLimit)
            val currentScreen = homeNavViewModel.homeNavigationView.value

            ZeneTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    HomepageView(songsViewModel)
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        songsViewModel.run()
    }

    override fun internetConnected() {
        songsViewModel.run()
    }
}