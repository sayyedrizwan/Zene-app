package com.rizwansayyed.zene.presenter.ui.home.mymusic

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.OFFICIAL_ADS_PAGE
import com.rizwansayyed.zene.utils.Utils.OFFICIAL_EMAIL
import com.rizwansayyed.zene.utils.Utils.OFFICIAL_INSTAGRAM_ACCOUNTS
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.utils.Utils.shareFeedback
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


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
        Spacer(Modifier.height(10.dp))
        TextThin(stringResource(R.string.share_your_feedback), Modifier.clickable {
            shareFeedback()
        })
        Spacer(Modifier.height(10.dp))
        TextThin(stringResource(R.string.advertise_with_us), Modifier.clickable {
            Uri.parse(OFFICIAL_ADS_PAGE).customBrowser()
        })
    }
}

@Composable
fun MyMusicDownloadView(homeNavViewModel: HomeNavViewModel) {

    fun openBrowser(link: String?) {
        Uri.parse(link).customBrowser()
    }

    if (homeNavViewModel.downloadsAppLists.value != null) Column(
        Modifier.fillMaxWidth(), Arrangement.Center
    ) {
        Spacer(Modifier.height(100.dp))

        TextSemiBold(
            homeNavViewModel.downloadsAppLists.value?.text ?: "",
            Modifier.padding(horizontal = 15.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            homeNavViewModel.downloadsAppLists.value?.lists?.forEach {
                AsyncImage(it?.img,
                    "Download",
                    Modifier
                        .padding(top = 5.dp)
                        .width((LocalConfiguration.current.screenWidthDp.dp / (2.7).dp).dp)
                        .clickable {
                            openBrowser(it?.link)
                        }
                        .padding(4.dp))
            }
        }
    }
}