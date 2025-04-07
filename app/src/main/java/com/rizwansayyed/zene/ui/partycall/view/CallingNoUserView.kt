package com.rizwansayyed.zene.ui.partycall.view

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.partycall.PartyCallActivity
import com.rizwansayyed.zene.ui.partycall.PartyViewModel
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.utils.MainUtils.vibratePhone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CallingNoUserView(partyViewModel: PartyViewModel, txt: Int) {
    val activity = LocalActivity.current
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        GlideImage(
            partyViewModel.profilePhoto,
            partyViewModel.name,
            Modifier
                .fillMaxSize()
                .blur(40.dp),
            contentScale = ContentScale.Crop
        )

        TextViewBold(stringResource(txt), 25, center = true)

        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp)
                .align(Alignment.BottomCenter),
            Arrangement.SpaceEvenly,
            Alignment.CenterVertically
        ) {
            ImageWithBgRound(
                R.drawable.ic_cancel_close, Color.Black, Color.White
            ) {
                activity?.finishAffinity()
            }

            ImageWithBgRound(
                R.drawable.ic_calling, Color.Green.copy(0.5f), Color.White
            ) {
                activity?.finish()
                CoroutineScope(Dispatchers.IO).launch {
                    delay(500)
                    Intent(activity, PartyCallActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra(Intent.EXTRA_EMAIL, partyViewModel.email)
                        putExtra(Intent.EXTRA_USER, partyViewModel.profilePhoto)
                        putExtra(Intent.EXTRA_PACKAGE_NAME, partyViewModel.name)
                        putExtra(Intent.EXTRA_MIME_TYPES, -1)
                        activity?.startActivity(this)
                    }
                    if (isActive) cancel()
                }
            }
        }

        LaunchedEffect(Unit) {
            stopRingtoneFromEarpiece()
            vibratePhone()
        }
    }
}