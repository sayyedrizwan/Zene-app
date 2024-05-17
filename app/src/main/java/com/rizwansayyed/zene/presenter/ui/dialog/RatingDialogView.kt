package com.rizwansayyed.zene.presenter.ui.dialog

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.support.v4.media.RatingCompat
import android.util.Log
import android.widget.RatingBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.isUserEverRatedApp
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.WhiteColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.openAppOnPlayStore
import com.rizwansayyed.zene.utils.Utils.shareFeedback
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun RatingDialogView() {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()
    val coroutines = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var showButtonShareType by remember { mutableIntStateOf(0) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    if (showDialog)
        AlertDialog({ showDialog = false }, {
            Column(
                Modifier
                    .background(MainColor)
                    .padding(6.dp),
                Arrangement.Center, Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))

                TextBold(
                    stringResource(id = R.string.do_you_like_this_app),
                    Modifier.fillMaxWidth(), true,
                    size = 21
                )

                Spacer(Modifier.height(20.dp))

                AndroidView({
                    RatingBar(it).apply {
                        rating = 0f
                        max = 5
                        stepSize = 1f
                        setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            if (fromUser) showButtonShareType = if (rating == 5f) 1 else 2
                        }
                    }
                })

                Spacer(Modifier.height(20.dp))

                if (showButtonShareType != 0) TextButton(
                    {
                        if (showButtonShareType == 1) {
                            openAppOnPlayStore()
                            registerEvent(FirebaseEvents.FirebaseEvent.RATING_CLICK_APP_STORE)
                        } else {
                            registerEvent(FirebaseEvents.FirebaseEvent.RATING_CLICK_FEEDBACK)
                            shareFeedback()
                        }
                        isUserEverRatedApp = flowOf(true)
                        showDialog = false
                    }, Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black)
                ) {
                    TextRegular(
                        if (showButtonShareType == 1) stringResource(R.string.review_on_play_store_help_us_to_grow)
                        else stringResource(R.string.share_your_feedback_to_improve),
                        Modifier.fillMaxWidth(), doCenter = true
                    )
                }

                Spacer(Modifier.height(5.dp))

            }
        }, containerColor = MainColor)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> coroutines.launch(Dispatchers.IO) {
                    delay(4.seconds)

                    if (!isUserEverRatedApp.first() && roomDbViewModel.roomDBList().size > 3)
                        showDialog = true

                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}