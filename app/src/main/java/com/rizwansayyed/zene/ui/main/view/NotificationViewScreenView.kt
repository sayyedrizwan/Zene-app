package com.rizwansayyed.zene.ui.main.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.openGoogleMapLocation
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NotificationViewScreenView(navViewModel: NavigationViewModel) {
    val notificationInfo = stringResource(R.string.click_on_notification_enable_switch)

    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(6.dp))
        TextViewSemiBold(
            stringResource(R.string.keep_up_with_your_entertainment), 18, center = true
        )
        Spacer(Modifier.height(6.dp))
        TextViewNormal(
            stringResource(R.string.enable_push_notification_to_get_update), 14, center = true
        )
        Spacer(Modifier.height(25.dp))
        GlideImage(
            R.raw.notification_scroll,
            "notification",
            Modifier.size(130.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(45.dp))
        ButtonHeavy(stringResource(R.string.enable_notification)) {
            notificationInfo.toast()
            notificationInfo.toast()
            openAppSettings()
            navViewModel.setHomeNavSections(HomeNavSelector.HOME)
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.clickable { navViewModel.setHomeNavSections(HomeNavSelector.HOME) }) {
            TextViewNormal(stringResource(R.string.maybe_later), 14, center = true)
        }
    }

    BackHandler {
        navViewModel.setHomeNavSections(HomeNavSelector.HOME)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationConnectLocationShare(navigationViewModel: NavigationViewModel) {
    ModalBottomSheet(
        { navigationViewModel.setHomeInfoNavigation(null) },
        contentColor = MainColor,
        containerColor = MainColor
    ) {
        Column(Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(10.dp))
            TextViewSemiBold(
                navigationViewModel.homeNotificationSection?.title?.substringBefore(". ") ?: "",
                16, center = true
            )
            Spacer(Modifier.height(20.dp))
            TextViewNormal(
                navigationViewModel.homeNotificationSection?.body ?: "", 14, center = true
            )
            Spacer(Modifier.height(20.dp))
            ButtonWithImageAndBorder(
                R.drawable.ic_location, R.string.open_location, Color.White, Color.White
            ) {
                val title =
                    navigationViewModel.homeNotificationSection?.title?.substringBefore(". ") ?: ""

                try {
                    val lat =
                        navigationViewModel.homeNotificationSection?.lat?.toDoubleOrNull() ?: 0.0
                    val lon =
                        navigationViewModel.homeNotificationSection?.long?.toDoubleOrNull() ?: 0.0
                    openGoogleMapLocation(false, lat, lon, title)
                } catch (e: Exception) {
                    e.message
                }
                navigationViewModel.setHomeInfoNavigation(null)
            }
            Spacer(Modifier.height(50.dp))
        }
    }
}