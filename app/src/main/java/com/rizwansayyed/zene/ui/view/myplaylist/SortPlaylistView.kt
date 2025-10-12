package com.rizwansayyed.zene.ui.view.myplaylist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.sortMyPlaylistTypeDB
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortPlaylistView(viewModel: MyLibraryViewModel, close: () -> Unit) {
    ModalBottomSheet(
        close,
        contentColor = MainColor,
        containerColor = MainColor,
        properties = ModalBottomSheetProperties()
    ) {
        val coroutine = rememberCoroutineScope()
        val sortMyPlaylistType by sortMyPlaylistTypeDB.collectAsState(SortMyPlaylistType.CUSTOM_ORDER)

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            TextViewBold(stringResource(R.string.sort_by), 19)

            Spacer(Modifier.height(30.dp))

            Row(
                Modifier
                    .padding(vertical = 10.dp)
                    .clickable {
                        coroutine.safeLaunch {
                            sortMyPlaylistTypeDB = flowOf(SortMyPlaylistType.CUSTOM_ORDER)
                        }
                    }) {
                TextViewNormal(stringResource(R.string.custom_order))
                Spacer(Modifier.weight(1f))
                if (sortMyPlaylistType == SortMyPlaylistType.CUSTOM_ORDER)
                    ImageIcon(R.drawable.ic_tick, 22)
            }

            Row(
                Modifier
                    .padding(vertical = 10.dp)
                    .clickable {
                        coroutine.safeLaunch {
                            sortMyPlaylistTypeDB = flowOf(SortMyPlaylistType.TITLE)
                        }
                    }) {
                TextViewNormal(stringResource(R.string.title))
                Spacer(Modifier.weight(1f))
                if (sortMyPlaylistType == SortMyPlaylistType.TITLE)
                    ImageIcon(R.drawable.ic_tick, 22)
            }

            Row(
                Modifier
                    .padding(vertical = 10.dp)
                    .clickable {
                        coroutine.safeLaunch {
                            sortMyPlaylistTypeDB = flowOf(SortMyPlaylistType.ARTIST)
                        }
                    }) {
                TextViewNormal(stringResource(R.string.artist))
                Spacer(Modifier.weight(1f))
                if (sortMyPlaylistType == SortMyPlaylistType.ARTIST)
                    ImageIcon(R.drawable.ic_tick, 22)
            }

            Row(Modifier
                .padding(vertical = 10.dp)
                .clickable {
                    coroutine.safeLaunch {
                        sortMyPlaylistTypeDB = flowOf(SortMyPlaylistType.RECENTLY_ADDED)
                    }
                }) {
                TextViewNormal(stringResource(R.string.recently_added))
                Spacer(Modifier.weight(1f))
                if (sortMyPlaylistType == SortMyPlaylistType.RECENTLY_ADDED)
                    ImageIcon(R.drawable.ic_tick, 22)
            }

            Spacer(Modifier.height(70.dp))
        }
    }
}

enum class SortMyPlaylistType {
    CUSTOM_ORDER, TITLE, ARTIST, RECENTLY_ADDED
}
