package com.rizwansayyed.zene.ui.main.search.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun TrendingItemView(data: ZeneMusicData?, click: () -> Unit) {
    data?.name?.let {
        Row(
            Modifier
                .padding(horizontal = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Black)
                .clickable { click() }
                .padding(horizontal = 14.dp, vertical = 6.dp),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_chart_line, 15)
            Spacer(Modifier.width(5.dp))
            TextViewNormal(it, 14)
        }
    }
}

@Composable
fun SearchKeywordsItemView(text: String, click: (Boolean) -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clickable { click(true) },
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Box(Modifier.weight(1f)) {
            TextViewNormal(text, 16, line = 1)
        }
        Box(Modifier
            .rotate(-50f)
            .clickable { click(false) }) {
            ImageIcon(R.drawable.ic_arrow_right, 17)
        }
    }
}

@Composable
fun SearchTopView() {
    Spacer(Modifier.height(70.dp))

    Row(
        Modifier.padding(horizontal = 9.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        TextViewBold(stringResource(R.string.search), 24)
        Spacer(Modifier.weight(1f))
        ImageIcon(R.drawable.ic_voice_id, 25)
        Spacer(Modifier.width(9.dp))
        ImageIcon(R.drawable.ic_mic, 25)
    }
}