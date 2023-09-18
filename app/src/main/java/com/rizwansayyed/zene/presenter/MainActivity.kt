package com.rizwansayyed.zene.presenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.MainHomePageView
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

class MainActivity : ComponentActivity() {

    private val navViewModel: HomeNavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)
        setContent {
            ZeneTheme {
                MainHomePageView()
            }
        }


        navViewModel.checkAndSetOnlineStatus()
    }
}
