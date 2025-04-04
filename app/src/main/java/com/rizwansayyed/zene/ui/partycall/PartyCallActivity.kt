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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.service.notification.clearCallNotification
import com.rizwansayyed.zene.ui.partycall.view.CalledPickedView
import com.rizwansayyed.zene.ui.partycall.view.CallingWebView
import com.rizwansayyed.zene.ui.partycall.view.stopRingtoneFromEarpiece
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.MainUtils.hasPIPPermission
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                val email = DataStorageManager.userInfo.collectAsState(null)

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MainColor)
                ) {
                    if (email.value?.email != null && partyViewModel.email.isNotEmpty() && partyViewModel.isCallPicked) {
                        CallingWebView(partyViewModel.email, email.value?.email!!, partyViewModel)
                    }
                }

//                if (!partyViewModel.hideCallingView) Box(
//                    Modifier
//                        .fillMaxSize()
//                        .background(MainColor)
//                ) {
//                    if (partyViewModel.profilePhoto.isNotEmpty()) GlideImage(
//                        partyViewModel.profilePhoto, partyViewModel.name, Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    if (!partyViewModel.isInPictureInPicture)
//                        CallingView(Modifier.align(Alignment.BottomCenter), partyViewModel)
//
//                    if (!partyViewModel.isInPictureInPicture)
//                        ButtonArrowBack(Modifier.align(Alignment.TopStart)) {
//                            goToPIP()
//                        }
//                }
//
//                if (partyViewModel.hideCallingView) CalledPickedView(viewModel)

                CalledPickedView(partyViewModel)

                LaunchedEffect(Unit) {
                    checkIntent(intent)
                    delay(1.seconds)
                    if (!partyViewModel.email.contains("@")) finish()
                }

                LaunchedEffect(partyViewModel.type) {
                    if (partyViewModel.type == -1) {
//                        playRingtoneFromEarpiece(this@PartyCallActivity, false)

                        delay(1.seconds)
                        viewModel.sendPartyCall(partyViewModel.email, partyViewModel.randomCode)
                    } else {
                        stopRingtoneFromEarpiece()
                    }
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

    private fun checkIntent(intent: Intent) = CoroutineScope(Dispatchers.IO).launch {
        val type = intent.getIntExtra(Intent.EXTRA_MIME_TYPES, 0)
        val code = intent.getStringExtra(Intent.EXTRA_TEXT)
        val email = intent.getStringExtra(Intent.EXTRA_EMAIL) ?: ""
        val profilePhoto = intent.getStringExtra(Intent.EXTRA_USER) ?: ""
        val name = intent.getStringExtra(Intent.EXTRA_PACKAGE_NAME) ?: ""

//        delay(500)
        partyViewModel.setInfo(profilePhoto, email, name, type)
        clearCallNotification(email)

        if (type == -1) {
            partyViewModel.generateAlphabetCodeSet()
//            delay(500)
            partyViewModel.setCallPicked()
        } else if (type == 1) {
            partyViewModel.setCallPicked()
            partyViewModel.hideCallingView()
        } else {
            if (code != null)
                partyViewModel.setCode(code)
            else
                finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRingtoneFromEarpiece()
    }
}