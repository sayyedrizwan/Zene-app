package com.rizwansayyed.zene.ui.login

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.login.dialog.LoginEmailSheet
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.FacebookColor
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.FirebaseEvents.FirebaseEventsParams
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_PRIVACY_POLICY
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@Composable
fun LoginView(viewModel: NavigationViewModel) {
    val loginUtils = viewModel.loginUtils
    val activity = LocalActivity.current as Activity
    var startEmailLogin by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(bottom = 80.dp)
            .fillMaxSize()
            .background(DarkCharcoal),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            TextViewSemiBold(stringResource(R.string.app_name), 50, center = true)
            Spacer(Modifier.height(1.dp))
            TextViewNormal(stringResource(R.string.app_desc), 14, center = true)
        }

        AnimatedVisibility(loginUtils.isLoading) {
            CircularLoadingView()
        }

        AnimatedVisibility(!loginUtils.isLoading) {
            Column(Modifier.fillMaxWidth()) {
                ButtonWithImageAndBorder(R.drawable.ic_google, R.string.continue_with_google) {
                    loginUtils.startGoogleLogin(activity)
                }
                Spacer(Modifier.height(24.dp))
                ButtonWithImageAndBorder(R.drawable.ic_apple, R.string.continue_with_apple) {
                    loginUtils.startAppleLogin(activity)
                }
                Spacer(Modifier.height(24.dp))
                ButtonWithImageAndBorder(R.drawable.ic_facebook, R.string.continue_with_facebook) {
                    loginUtils.startFacebookLogin(activity)
                }
                Spacer(Modifier.height(24.dp))
                ButtonWithImageAndBorder(R.drawable.ic_email_auth, R.string.continue_with_email) {
                    startEmailLogin = true
                }
                Spacer(Modifier.height(34.dp))

                Box(
                    Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            MediaContentUtils.openCustomBrowser("$ZENE_URL$ZENE_URL_PRIVACY_POLICY")
                        }) {
                    TextViewLight(
                        stringResource(R.string.by_login_your_are_accepting_privacy_policy),
                        13, center = true, color = FacebookColor
                    )
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        registerEvents(FirebaseEventsParams.STARTED_LOGIN_VIEW)
    }

    if (startEmailLogin) LoginEmailSheet(loginUtils) {
        startEmailLogin = false
    }
}