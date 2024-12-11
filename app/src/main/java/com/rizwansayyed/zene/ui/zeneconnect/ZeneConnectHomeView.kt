package com.rizwansayyed.zene.ui.zeneconnect

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.home.view.TextTitleHeader
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerActivity
import com.rizwansayyed.zene.ui.view.SmallButtonBorderText

@Composable
fun ZeneConnectHomeView() {
    val context = LocalContext.current.applicationContext
    val info by userInfoDB.collectAsState(initial = null)

    TextTitleHeader(Pair(TextSize.BIG, R.string.zene_connect))

    if (info?.phonenumber == null && info?.countryCode == null) {
        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            SmallButtonBorderText(R.string.verify_phone_number_to_continue) {
                Intent(context, TrueCallerActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(this)
                }
            }
        }
    }
}