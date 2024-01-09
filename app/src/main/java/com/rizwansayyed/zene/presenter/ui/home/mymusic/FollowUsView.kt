package com.rizwansayyed.zene.presenter.ui.home.mymusic

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.OFFICIAL_INSTAGRAM_ACCOUNTS
import com.rizwansayyed.zene.utils.Utils.customBrowser

@Composable
fun FollowUs() {
    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(100.dp))
        SmallIcons(icon = R.drawable.ic_instagram, 26, 0) {
            registerEvent(FirebaseEvents.FirebaseEvent.OPEN_INSTAGRAM_ACCOUNT)
            Uri.parse(OFFICIAL_INSTAGRAM_ACCOUNTS).customBrowser()
        }
        Spacer(Modifier.height(10.dp))
        TextThin(stringResource(R.string.follow_us_on_instagram))
    }
}