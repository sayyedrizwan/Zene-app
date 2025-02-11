package com.rizwansayyed.zene.ui.main.search.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun TrendingItemView(data: ZeneMusicData?) {
    data?.name?.let {
        Row(
            Modifier
                .padding(horizontal = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Black)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            Arrangement.Center, Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_chart_line, 15)
            Spacer(Modifier.width(5.dp))
            TextViewNormal(it, 14)
        }
    }
}