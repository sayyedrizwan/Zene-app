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
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.OFFICIAL_EMAIL
import com.rizwansayyed.zene.utils.Utils.OFFICIAL_INSTAGRAM_ACCOUNTS
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


@Composable
fun FollowUs() {
    val context = LocalContext.current.applicationContext
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
            Intent(Intent.ACTION_SEND).apply {
                setType("plain/text")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(OFFICIAL_EMAIL))
                putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                putExtra(Intent.EXTRA_TEXT, "")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        })
    }
}

//Remember what Taylor Swift said, 'haters gonna hate, hate, hate.' So take a cue from the queen of pop and love yourself!

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