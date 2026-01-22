package com.rizwansayyed.zene.ui.main.ent.view

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector
import com.rizwansayyed.zene.ui.main.home.view.TextSimpleCards
import com.rizwansayyed.zene.utils.MainUtils
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@Composable
fun EntertainmentScreenTopView(viewModel: NavigationViewModel) {
    Row(
        Modifier
            .padding(top = if (MainUtils.isDirectToTV()) 15.dp else 60.dp)
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(10.dp))

        TextSimpleCards(
            viewModel.entNavSection == EntSectionSelector.DISCOVER,
            stringResource(R.string.discover)
        ) {
            viewModel.setEntNavigation(EntSectionSelector.DISCOVER)
        }

        TextSimpleCards(
            viewModel.entNavSection == EntSectionSelector.BUZZ,
            stringResource(R.string.buzz)
        ) {
            viewModel.setEntNavigation(EntSectionSelector.BUZZ)
        }

        TextSimpleCards(
            viewModel.entNavSection == EntSectionSelector.DATING,
            stringResource(R.string.dating)
        ) {
            viewModel.setEntNavigation(EntSectionSelector.DATING)
        }

        TextSimpleCards(
            viewModel.entNavSection == EntSectionSelector.MOVIES,
            stringResource(R.string.movies)
        ) {
            viewModel.setEntNavigation(EntSectionSelector.MOVIES)
        }

//        TextSimpleCards(
//            viewModel.entNavSection == EntSectionSelector.EVENTS,
//            stringResource(R.string.events)
//        ) {
//            viewModel.setEntNavigation(EntSectionSelector.EVENTS)
//        }

        TextSimpleCards(
            viewModel.entNavSection == EntSectionSelector.LIFESTYLE,
            stringResource(R.string.lifestyle)
        ) {
            viewModel.setEntNavigation(EntSectionSelector.LIFESTYLE)
        }
    }
}