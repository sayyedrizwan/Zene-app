package com.rizwansayyed.zene.service.partycall

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PartyCallActivity : FragmentActivity() {

    private val viewModel: ConnectViewModel by viewModels()

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

//        viewModel.sendPartyCall(email)

        type.toast()
    }
}