package com.rizwansayyed.zene.ui.settings.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.StarRatingBar
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.openAppOnPlayStore
import com.rizwansayyed.zene.utils.MainUtils.openFeedbackMail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsShareSheetView(close: () -> Unit) {
    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), contentColor = MainColor, containerColor = MainColor
    ) {
        var rating by remember { mutableFloatStateOf(0f) }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp),
            Arrangement.Center, Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.Start) {
                TextViewSemiBold(stringResource(R.string.rate_us))
                TextViewNormal(stringResource(R.string.rate_us_desc))
            }
            Spacer(Modifier.height(20.dp))

            StarRatingBar(5, rating) {
                rating = it
            }

            Spacer(Modifier.height(20.dp))

            if (rating == 5f)
                ButtonHeavy(stringResource(R.string.love_the_app_rate_us_on_play_store), BlackGray) {
                    openAppOnPlayStore()
                    close()
                }
            else if (rating > 1f)
                ButtonHeavy(stringResource(R.string.no_5_star_share_feedback), BlackGray) {
                    openFeedbackMail()
                    close()
                }

            Spacer(Modifier.height(70.dp))
        }
    }
}
