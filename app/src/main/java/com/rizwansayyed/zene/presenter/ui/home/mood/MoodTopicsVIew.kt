package com.rizwansayyed.zene.presenter.ui.home.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MoodData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.mood.MoodTopics.moodList
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

object MoodTopics {
    private val happy = context.resources.getString(R.string.happy)
    private val party = context.resources.getString(R.string.party)
    private val angry = context.resources.getString(R.string.angry)
    private val rock = context.resources.getString(R.string.rock)
    private val stressed = context.resources.getString(R.string.stressed)
    private val pop = context.resources.getString(R.string.pop)
    private val focused = context.resources.getString(R.string.focused)
    private val workout = context.resources.getString(R.string.workout)
    private val sleepy = context.resources.getString(R.string.sleepy)
    private val feelGood = context.resources.getString(R.string.feel_good)
    private val jazz = context.resources.getString(R.string.jazz)
    private val romance = context.resources.getString(R.string.romance)
    private val sad = context.resources.getString(R.string.sad)

    val moodList = listOf(
        MoodData("\uD83D\uDE0A $happy", happy, ""),
        MoodData("\uD83E\uDD73 $party", party, ""),
        MoodData("\uD83D\uDE24 $angry", angry, ""),
        MoodData("\uD83E\uDD18 $rock", rock, ""),
        MoodData("\uD83D\uDE2C $stressed", stressed, ""),
        MoodData("\uD83C\uDF8A $pop", pop, ""),
        MoodData("\uD83C\uDFAF $focused", focused, ""),
        MoodData("\uD83C\uDFCB️ $workout", workout, ""),
        MoodData("\uD83D\uDE29 $sleepy", sleepy, ""),
        MoodData("\uD83D\uDE0C $feelGood", feelGood, ""),
        MoodData("\uD83C\uDFB7 $jazz", jazz, ""),
        MoodData("\uD83E\uDD70 $romance", romance, ""),
        MoodData("\uD83D\uDE14 $sad", sad, "")
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodTopics(homeNavModel: HomeNavViewModel) {
    TopInfoWithSeeMore(R.string.mood, null) {}

    FlowRow {
        moodList.forEach {
            Column(
                Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MainColor)
                    .padding(vertical = 10.dp, horizontal = 22.dp)
                    .clickable {
                        homeNavModel.setTheMood(it)
                    }
            ) {
                TextRegular(it.name, size = 14)
            }
        }
    }
}