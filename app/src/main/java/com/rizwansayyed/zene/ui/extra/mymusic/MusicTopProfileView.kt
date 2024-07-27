package com.rizwansayyed.zene.ui.extra.mymusic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.ipDB
import com.rizwansayyed.zene.data.db.model.UserInfoData
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.TextPoppinsThin

@Composable
fun MusicTopProfileView(profile: UserInfoData?) {
    val ip by ipDB.collectAsState(initial = null)

    Spacer(Modifier.height(20.dp))

    TextPoppins(profile?.name ?: "Zene User", size = 50)
    Spacer(Modifier.height(1.dp))
    TextPoppins(profile?.email ?: "", size = 15)

    Row(Modifier.offset(y = (-15).dp), Arrangement.Center, Alignment.CenterVertically) {
        ImageIcon(R.drawable.ic_location, 20)
        Spacer(Modifier.width(1.dp))
        TextPoppins(ip?.country ?: "", size = 15)
    }
}

@Composable
fun InfoOf(profile: UserInfoData?) {
    Spacer(Modifier.height(40.dp))
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
        Column(Modifier, Arrangement.Center, Alignment.CenterHorizontally) {
            TextPoppins(v = profile?.totalPlaytime() ?: "0m", size = 17, lineHeight = 10)
            TextPoppinsSemiBold(v = "PlayTime", size = 19)
        }

        Column(Modifier, Arrangement.Center, Alignment.CenterHorizontally) {
            TextPoppins(v = "12", size = 17, lineHeight = 10)
            TextPoppinsSemiBold(v = "Following", size = 19)
        }
    }

    Spacer(Modifier.height(40.dp))

    Button(onClick = { }, Modifier.padding(horizontal = 5.dp), colors = ButtonDefaults.buttonColors(Color.Black)) {
        TextPoppinsSemiBold(stringResource(R.string.edit_profile), size = 15)
    }
}