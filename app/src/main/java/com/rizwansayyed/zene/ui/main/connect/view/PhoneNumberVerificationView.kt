package com.rizwansayyed.zene.ui.main.connect.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.phoneverification.PhoneVerificationActivity
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun PhoneNumberVerificationView() {
    val context = LocalContext.current.applicationContext
    Column(
        Modifier
            .padding(top = 15.dp)
            .padding(15.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(13.dp))
            .background(Color.Black)
            .padding(5.dp)
            .padding(vertical = 35.dp)
            .padding(horizontal = 8.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        TextViewSemiBold(
            stringResource(R.string.verify_your_phone_number_to_enjoy_connect), 15, center = true
        )
        Spacer(Modifier.height(15.dp))

        FilledTonalButton({
            Intent(context, PhoneVerificationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }) {
            TextViewNormal(stringResource(R.string.verify), 14, line = 1)
        }
    }
}