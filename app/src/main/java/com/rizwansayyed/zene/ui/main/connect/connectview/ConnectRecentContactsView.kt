package com.rizwansayyed.zene.ui.main.connect.connectview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.TextViewBold

@Composable
fun ConnectRecentContactsView() {
    Spacer(Modifier.height(52.dp))
    Row(Modifier.padding(horizontal = 9.dp)) {
        TextViewBold(stringResource(R.string.recent), 18)
    }
    Spacer(Modifier.height(12.dp))


}