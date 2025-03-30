package com.rizwansayyed.zene.service.partycall

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class PartyCallActivity : FragmentActivity() {

    private val viewModel: ConnectViewModel by viewModels()

    private var profilePhoto by mutableStateOf("")
    private var name by mutableStateOf("")
    private var email by mutableStateOf("")

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
                        profilePhoto, name, Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                LaunchedEffect(Unit) {
                    delay(1.seconds)
                    if (!email.contains("@")) finish()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkIntent(intent)
    }

    private fun checkIntent(intent: Intent) {
        val type = intent.getIntExtra(Intent.EXTRA_MIME_TYPES, 0)
        val email = intent.getStringExtra(Intent.EXTRA_EMAIL) ?: ""
        val profilePhoto = intent.getStringExtra(Intent.EXTRA_USER) ?: ""
        this.profilePhoto = profilePhoto
        this.email = email
//        viewModel.sendPartyCall(email)

        type.toast()
    }
}