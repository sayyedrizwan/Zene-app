package com.rizwansayyed.zene.ui.partycall

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.service.notification.clearCallNotification
import com.rizwansayyed.zene.service.party.PartyService
import com.rizwansayyed.zene.ui.partycall.view.CallingView
import com.rizwansayyed.zene.ui.partycall.view.playRingtoneFromEarpiece
import com.rizwansayyed.zene.ui.partycall.view.stopRingtoneFromEarpiece
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.utils.MainUtils.hasPIPPermission
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class PartyCallActivity : FragmentActivity() {

    private val viewModel: ConnectViewModel by viewModels()
    private val partyViewModel: PartyViewModel by viewModels()


    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                checkIntent(intent)
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MainColor)
                ) {

                    GlideImage(
                        partyViewModel.profilePhoto, partyViewModel.name, Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (!partyViewModel.isInPictureInPicture)
                        CallingView(Modifier.align(Alignment.BottomCenter), partyViewModel)

                    if (!partyViewModel.isInPictureInPicture)
                        ButtonArrowBack(Modifier.align(Alignment.TopStart)) {
                            goToPIP()
                        }
                }

                LaunchedEffect(Unit) {
                    delay(1.seconds)
                    if (!partyViewModel.email.contains("@")) finish()
                }
            }


            BackHandler { goToPIP() }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        goToPIP()
    }

    private fun goToPIP() {
        val needPictureMode = resources.getString(R.string.pip_mode_disabled_for_app)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!hasPIPPermission()) {
                needPictureMode.toast()
                openAppSettings()
                return
            }
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(50, 50)).build()

            enterPictureInPictureMode(params)
        } else
            finish()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        partyViewModel.setPIP(isInPictureInPictureMode)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkIntent(intent)
    }

    private fun checkIntent(intent: Intent) {
        val type = intent.getIntExtra(Intent.EXTRA_MIME_TYPES, 0)
        val email = intent.getStringExtra(Intent.EXTRA_EMAIL) ?: ""
        val profilePhoto = intent.getStringExtra(Intent.EXTRA_USER) ?: ""
        val name = intent.getStringExtra(Intent.EXTRA_PACKAGE_NAME) ?: ""

        partyViewModel.setInfo(profilePhoto, email, name, type)

        clearCallNotification(email)
        if (type == -1) {
            playRingtoneFromEarpiece(this, false)
            viewModel.sendPartyCall(email)
        }


        Intent(this, PartyService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startService(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRingtoneFromEarpiece()
    }
}