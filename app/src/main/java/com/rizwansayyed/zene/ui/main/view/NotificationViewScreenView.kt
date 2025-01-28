package com.rizwansayyed.zene.ui.main.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NotificationViewScreenView(navViewModel: NavigationViewModel) {
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