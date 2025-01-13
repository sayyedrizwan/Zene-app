package com.rizwansayyed.zene.ui.phoneverification.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun TrueCallerVerifyView() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp), Alignment.Center
    ) {
        Column(Modifier.fillMaxWidth()) {
            TextViewSemiBold(stringResource(R.string.verify_phone_number), 19)
            Spacer(Modifier.height(7.dp))
            TextViewNormal(stringResource(R.string.verify_phone_number_desc), 16)
            Spacer(Modifier.height(55.dp))

            ButtonHeavy(R.string.verify) {

            }
        }
    }
}