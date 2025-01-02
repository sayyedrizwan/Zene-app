package com.rizwansayyed.zene.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun LoginView() {
    Column(
        Modifier
            .padding(bottom = 100.dp)
            .fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextViewSemiBold(stringResource(R.string.app_name), 50, center = true)
            Spacer(Modifier.height(1.dp))
            TextViewNormal(stringResource(R.string.app_desc), 14, center = true)
        }

        ButtonWithImageAndBorder(R.drawable.ic_google, R.string.continue_with_google) {

        }
        Spacer(Modifier.height(24.dp))
        ButtonWithImageAndBorder(R.drawable.ic_apple, R.string.continue_with_apple) {

        }
        Spacer(Modifier.height(24.dp))
        ButtonWithImageAndBorder(R.drawable.ic_facebook, R.string.continue_with_facebook) {

        }
        Spacer(Modifier.height(24.dp))
        Row(Modifier.clickable { }) {
            TextViewNormal(stringResource(R.string.continue_with_email), 16, center = true)
        }
    }
}