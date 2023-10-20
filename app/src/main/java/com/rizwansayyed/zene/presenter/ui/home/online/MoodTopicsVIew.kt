package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore

private val list = listOf(
    "\uD83D\uDE0A Happy",
    "\uD83E\uDD73 Party",
    "\uD83D\uDE24 Angry",
    "\uD83E\uDD18 Rock",
    "\uD83D\uDE2C Stressed",
    "\uD83C\uDF8A Pop",
    "\uD83C\uDFAF Focused",
    "\uD83C\uDFCB️ Workout",
    "\uD83D\uDE29 Sleepy",
    "\uD83D\uDE0C Feel Good",
    "\uD83C\uDFB7 Jazz",
    "\uD83E\uDD70 Romance",
    "\uD83D\uDE14 Sad"
)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodTopics() {
    TopInfoWithSeeMore(R.string.mood, null) {}

    FlowRow {
        list.forEach {
            Column(
                Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MainColor)
                    .padding(vertical = 10.dp, horizontal = 22.dp)
            ) {
                TextRegular(it, size = 14)
            }
        }
    }
}