package com.rizwansayyed.zene.ui.partycall

import android.Manifest
import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.service.notification.clearCallNotification
import com.rizwansayyed.zene.ui.partycall.view.CallingNoUserView
import com.rizwansayyed.zene.ui.partycall.view.CallingView
import com.rizwansayyed.zene.ui.partycall.view.CallingWebView
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class PartyCallActivity : FragmentActivity(), DeclinePartyCallInterface {

    companion object {
        var declinePartyCallInterface: DeclinePartyCallInterface? = null
    }

    private val viewModel: ConnectViewModel by viewModels()
    private val partyViewModel: PartyViewModel by viewModels()
    private val partySongSocketModel: PartySongSocketModel by viewModels()

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        declinePartyCallInterface = this

        setContent {
            ZeneTheme {
                val email by DataStorageManager.userInfo.collectAsState(null)

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MainColor)
                ) {
                    if (email?.email != null && partyViewModel.email.isNotEmpty() && partyViewModel.isCallPicked) {
                        CallingWebView(email?.name!!, email?.photo!!, partyViewModel)
                    }
                }

                if (!partyViewModel.hideCallingView) Box(
                    Modifier
                        .fillMaxSize()
                        .background(MainColor)
                ) {
                    if (partyViewModel.profilePhoto.isNotEmpty()) GlideImage(
                        partyViewModel.profilePhoto, partyViewModel.name, Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (!partyViewModel.isPIPMode)
                        CallingView(Modifier.align(Alignment.BottomCenter), partyViewModel)

                    if (!partyViewModel.isPIPMode)
                        ButtonArrowBack(Modifier.align(Alignment.TopStart)) {
                            goToPIP()
                        }
                }


                if (partyViewModel.noUsers) CallingNoUserView(partyViewModel, R.string.call_ended)
                if (partyViewModel.callDeclined)
                    CallingNoUserView(partyViewModel, R.string.call_declined)
                if (partyViewModel.noCallAnswered)
                    CallingNoUserView(partyViewModel, R.string.call_not_answered)

                LaunchedEffect(Unit) {
                    checkIntent(intent)
                    delay(1.seconds)
                    if (!partyViewModel.email.contains("@")) finish()
                }

                LaunchedEffect(partyViewModel.randomCallCode) {
                    delay(1.seconds)
                    if (partyViewModel.randomCallCode.trim().length > 2)
                        partySongSocketModel.connect(partyViewModel.randomCallCode.trim())
                }

                LaunchedEffect(partyViewModel.type) {
                    if (partyViewModel.type == -1) {
                        playRingtoneFromEarpiece(this@PartyCallActivity, false)
                        delay(1.seconds)
                        viewModel.sendPartyCall(partyViewModel.email, partyViewModel.randomCallCode)
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
        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            partyViewModel.setCallEnded()
            lifecycleScope.launch {
                delay(500)
                finishAffinity()
            }
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        partyViewModel.setPIP(isInPictureInPictureMode)

        if (isInPictureInPictureMode && partyViewModel.noUsers) {
            finishAffinity()
        }
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

        partyViewModel.setInfo(profilePhoto, email, name, type)
        clearCallNotification(email)

        if (type == -1) {
            partyViewModel.setCode(null)
            delay(500)
            partyViewModel.setCallPicked()
            partyViewModel.startJob()
        }

        if (code != null) partyViewModel.setCode(code)

        if (type == 1 && isCameraAndMicrophonePermissionEnabled()) {
            partyViewModel.setCallPicked()
            partyViewModel.hideCallingView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        declinePartyCallInterface = null
        stopRingtoneFromEarpiece()
    }

    private fun isCameraAndMicrophonePermissionEnabled(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun declineCall(email: String?) {
        if (partyViewModel.email == email) partyViewModel.setCallDeclined()
    }

    override fun changeUpdate(v: ZeneMusicData) {
        partySongSocketModel.sendPartyJson(v)
    }

}