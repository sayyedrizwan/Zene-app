package com.rizwansayyed.zene.ui.home.view

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.RatingBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.appReviewStatusDB
import com.rizwansayyed.zene.data.db.model.AppReviewData
import com.rizwansayyed.zene.data.db.model.AppReviewEnum
import com.rizwansayyed.zene.ui.extra.playlists.AddPlaylistView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.feedbackMail
import com.rizwansayyed.zene.utils.Utils.openAppPageOnPlayStore
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds


@Composable
fun ReviewAppDialog() {
    var reviewDialog by remember { mutableStateOf(false) }
    var loveTheAppDialog by remember { mutableStateOf(false) }
    var dislikedTheAppDialog by remember { mutableStateOf(false) }

    if (reviewDialog) Row(
        Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Black)
            .padding(vertical = 25.dp, horizontal = 15.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Row(
            Modifier.weight(1f),
            Arrangement.Start, Alignment.CenterVertically,
        ) {
            TextPoppins(stringResource(R.string.do_you_like_the_app), size = 18)
        }

        Row {
            Box(Modifier.clickable { loveTheAppDialog = true }) {
                TextPoppins("\uD83D\uDE0A", size = 27)
            }
            Spacer(Modifier.width(16.dp))
            Box(Modifier.clickable {
                dislikedTheAppDialog = true
                val d = AppReviewData(AppReviewEnum.FEEDBACK, System.currentTimeMillis())
                appReviewStatusDB = flowOf(d)
            }) {
                TextPoppins("\uD83D\uDE14", size = 27)
            }
            Spacer(Modifier.width(5.dp))
        }
    }

    if (dislikedTheAppDialog) DislikeUsAppDialog {
        dislikedTheAppDialog = false
        if (it) reviewDialog = false
    }

    if (loveTheAppDialog) LikeUsAppDialog {
        loveTheAppDialog = false
        if (it) reviewDialog = false
    }

    LaunchedEffect(reviewDialog) {
        if (reviewDialog) logEvents(FirebaseLogEvents.FirebaseEvents.VISIBLE_REVIEW_DIALOG)
    }

    LaunchedEffect(Unit) {
        val appReviewDB = appReviewStatusDB.firstOrNull()
        if (appReviewDB == null) {
            reviewDialog = true
            return@LaunchedEffect
        }
        if (appReviewDB.type == AppReviewEnum.DONE) return@LaunchedEffect

        if (appReviewDB.atTimestamp == null) {
            reviewDialog = true
            return@LaunchedEffect
        }
        val diffInMillis = (appReviewDB.atTimestamp ?: 0) - System.currentTimeMillis()
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        if (hours > 96) reviewDialog = true
    }
}

@Composable
private fun DislikeUsAppDialog(close: (Boolean) -> Unit) {
    val context = LocalContext.current.applicationContext

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { close(false) }
    ) {
        val windowManager =
            remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

        val metrics = DisplayMetrics().apply {
            windowManager.defaultDisplay.getRealMetrics(this)
        }
        val (width, _) = with(LocalDensity.current) {
            Pair(metrics.widthPixels.toDp(), metrics.heightPixels.toDp())
        }

        Column(
            modifier = Modifier
                .requiredSize(width - 120.dp, width)
                .clip(RoundedCornerShape(14.dp))
                .background(color = Color.White)
                .padding(8.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextPoppinsSemiBold(
                stringResource(R.string.having_issue_please_mail_us_help), true, Color.Black, 16
            )

            Spacer(Modifier.height(9.dp))

            Row(
                Modifier
                    .padding(5.dp)
                    .border(BorderStroke(1.dp, MainColor), RoundedCornerShape(25))
                    .clickable {
                        close(true)
                        feedbackMail()
                    }
                    .padding(vertical = 7.dp, horizontal = 10.dp),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                TextPoppins(
                    stringResource(id = R.string.mail_us), true, Color.Black, size = 15
                )
            }
        }
    }
}

@Composable
private fun LikeUsAppDialog(close: (Boolean) -> Unit) {
    val context = LocalContext.current as Activity
    var rating by remember { mutableFloatStateOf(0f) }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { close(false) }
    ) {
        val windowManager =
            remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

        val metrics = DisplayMetrics().apply {
            windowManager.defaultDisplay.getRealMetrics(this)
        }
        val (width, _) = with(LocalDensity.current) {
            Pair(metrics.widthPixels.toDp(), metrics.heightPixels.toDp())
        }

        Column(
            modifier = Modifier
                .requiredSize(width - 120.dp, width)
                .clip(RoundedCornerShape(14.dp))
                .background(color = Color.White)
                .padding(8.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            TextPoppinsSemiBold(
                stringResource(R.string.seem_enjoyed_the_app),
                true, Color.Black, 16
            )

            Spacer(Modifier.height(9.dp))

            AndroidView({
                RatingBar(it).apply {
                    numStars = 5
                    stepSize = 1f
                    progressTintList = ColorStateList.valueOf(Color.Yellow.toArgb())
                    setOnRatingBarChangeListener { ratingBar, _, _ ->
                        rating = ratingBar.rating
                    }
                }
            })

            if (rating > 0) {
                TextPoppinsSemiBold(
                    stringResource(if (rating == 5f) R.string.seem_enjoyed_the_app else R.string.help_us_by_sharing_your_thoughts),
                    true, Color.Black, 16
                )

                Spacer(Modifier.height(9.dp))

                Row(
                    Modifier
                        .padding(5.dp)
                        .border(BorderStroke(1.dp, MainColor), RoundedCornerShape(25))
                        .clickable {
                            if (rating < 5f) {
                                val d = AppReviewData(
                                    AppReviewEnum.FEEDBACK, System.currentTimeMillis()
                                )
                                appReviewStatusDB = flowOf(d)
                                close(true)
                                feedbackMail()
                                return@clickable
                            }
                            val d = AppReviewData(AppReviewEnum.DONE, null)
                            appReviewStatusDB = flowOf(d)
                            close(true)
                            openAppPageOnPlayStore()
                        }
                        .padding(vertical = 7.dp, horizontal = 10.dp),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    TextPoppins(
                        if (rating == 5f) stringResource(id = R.string.post_a_review_on_play_store)
                        else stringResource(id = R.string.mail_us), true, Color.Black, size = 15
                    )
                }
            }
        }
    }
}