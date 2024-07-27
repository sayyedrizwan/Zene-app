package com.rizwansayyed.zene.ui.extra.mymusic

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryResponse
import com.rizwansayyed.zene.ui.view.CardsViewDesc
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold

@Composable
fun MusicSongHistoryView(data: ZeneMusicHistoryResponse, toNext: () -> Unit) {
    TextPoppinsSemiBold(stringResource(R.string.songs_history), size = 15)

    LazyRow {
        items(data) {
            CardsViewDesc(it.asMusicData(), listOf(it.asMusicData()))
        }
    }
}