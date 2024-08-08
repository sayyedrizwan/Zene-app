package com.rizwansayyed.zene.ui.home.view

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
    var rating by remember { mutableFloatStateOf(0f) }
    var reviewDialog by remember { mutableStateOf(false) }

    if (reviewDialog) Dialog(properties = DialogProperties(
        usePlatformDefaultWidth = false,
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ), onDismissRequest = { reviewDialog = false }) {
        val context = LocalContext.current
        val windowManager =
            remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

        val metrics = DisplayMetrics().apply {
            windowManager.defaultDisplay.getRealMetrics(this)
        }
        val (width, height) = with(LocalDensity.current) {
            Pair(metrics.widthPixels.toDp(), metrics.heightPixels.toDp())
        }

        Column(
            modifier = Modifier
                .requiredSize(width - 120.dp, height / 2)
                .clip(RoundedCornerShape(14.dp))
                .background(color = Color.White)
                .padding(8.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {

            TextPoppinsSemiBold(
                stringResource(R.string.please_give_us_feedback_on_the_app), true, Color.Black, 15
            )

            Spacer(Modifier.height(10.dp))

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
                Row(
                    Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(13.dp))
                        .background(MainColor)
                        .clickable {
                            if (rating >= 5) {
                                openAppPageOnPlayStore()
                                val d = AppReviewData(AppReviewEnum.DONE, null)
                                appReviewStatusDB = flowOf(d)
                            } else {
                                val d = AppReviewData(
                                    AppReviewEnum.FEEDBACK, System.currentTimeMillis()
                                )
                                appReviewStatusDB = flowOf(d)
                                feedbackMail()
                            }
                            reviewDialog = false
                        }
                        .padding(vertical = 15.dp, horizontal = 15.dp),
                    Arrangement.Center,
                    Alignment.CenterVertically) {
                    if (rating >= 5) TextPoppins(
                        stringResource(id = R.string.like_the_app_rate_us_on_play_store),
                        true,
                        size = 15
                    )
                    else TextPoppins(
                        stringResource(id = R.string.having_issue_please_mail_us_help),
                        true,
                        size = 15
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(2.seconds)
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