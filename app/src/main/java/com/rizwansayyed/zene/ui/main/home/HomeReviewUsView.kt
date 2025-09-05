package com.rizwansayyed.zene.ui.main.home

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.play.core.review.ReviewManagerFactory
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.isPostedReviewDB
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.safeLaunch
import kotlinx.coroutines.flow.flowOf
import kotlin.getValue

@Composable
fun HomeReviewUsView() {
    val context = LocalActivity.current
    val manager by lazy { ReviewManagerFactory.create(context!!.applicationContext) }

    val isAlreadyGivenReview by isPostedReviewDB.collectAsState(true)
    var timeTookReview by remember { mutableLongStateOf(0L) }

    val coroutine = rememberCoroutineScope()


    if (!isAlreadyGivenReview) Row(
        Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .padding(15.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            TextViewSemiBold(stringResource(R.string.happy_with_the_app), size = 21)
            TextViewNormal(stringResource(R.string.happy_with_the_app_desc), size = 16)
        }
        Spacer(Modifier.width(10.dp))
        TextViewSemiBold("\uD83D\uDE14", size = 30)
        Spacer(Modifier.width(10.dp))
        Box(Modifier.clickable {
            timeTookReview = System.currentTimeMillis()
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(context!!, reviewInfo)

                    flow.addOnCompleteListener { _ ->
                        val durationMillis = System.currentTimeMillis() - timeTookReview
                        val durationSeconds = durationMillis / 1000.0
                        if (durationSeconds <= 3) {
                            context.resources.getString(R.string.please_give_us_good_review).toast()
                            MainUtils.openAppOnPlayStore()
                        }
                        coroutine.safeLaunch {
                            isPostedReviewDB = flowOf(true)
                        }
                    }
                } else {
                    MainUtils.openAppOnPlayStore()
                }
            }
        }) {
            TextViewSemiBold("\uD83D\uDE0A", size = 30)
        }
    }
}

