package com.rizwansayyed.zene.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.ZeneUpdateAvailabilityItem
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.ui.view.shimmerEffectBrush
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.openAppPageOnPlayStore

@Composable
fun UpdateAvailableViewLoading() {
    Spacer(
        Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(40))
            .background(shimmerEffectBrush())
    )
}

@Composable
fun UpdateAvailableView(item: ZeneUpdateAvailabilityItem) {
    Row(
        Modifier
            .padding(vertical = 30.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .clickable {
                openAppPageOnPlayStore()
                FirebaseLogEvents.logEvents(FirebaseLogEvents.FirebaseEvents.UPDATE_AVAILABLE_CARD_CLICKED)
            }
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Black)
            .padding(vertical = 25.dp, horizontal = 15.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        TextPoppins(stringResource(R.string.app_update_available), size = 16)

        Row(
            Modifier
                .padding(horizontal = 5.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Red.copy(0.9f))
                .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
            TextPoppinsSemiBold(item.appVersion ?: "", size = 13)
        }

        Spacer(Modifier.weight(1f))

        Box(Modifier.rotate(180f)) {
            ImageIcon(id = R.drawable.ic_arrow_left, size = 25)
        }
    }
}