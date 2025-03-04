package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun HomeLoveTextView() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        TextViewBoldBig(stringResource(R.string.app_desc_bottom), 27)
        Spacer(Modifier.height(10.dp))
        TextViewNormal(stringResource(R.string.app_desc_bottom_desc), 18)
    }
}