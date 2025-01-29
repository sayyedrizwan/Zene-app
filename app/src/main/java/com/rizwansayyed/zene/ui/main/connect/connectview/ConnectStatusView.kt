@file:JvmName("ConnectStatusViewKt")

package com.rizwansayyed.zene.ui.main.connect.connectview

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.connect_status.ConnectStatusActivity
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold

@Composable
fun ConnectStatusTopView() {
    val context = LocalContext.current.applicationContext
    Spacer(Modifier.height(42.dp))
    Row(Modifier.padding(horizontal = 9.dp)) {
        TextViewBold(stringResource(R.string.status), 18)
        Spacer(Modifier.weight(1f))
        Box(Modifier.clickable {
            Intent(context, ConnectStatusActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }) {
            ImageIcon(R.drawable.ic_layer_add, 23)
        }
    }
    Spacer(Modifier.height(12.dp))
}

