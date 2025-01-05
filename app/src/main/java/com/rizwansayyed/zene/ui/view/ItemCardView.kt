package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.data.model.ZeneMusicData

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCardView(data: ZeneMusicData?) {
    Column(
        Modifier
            .padding(horizontal = 9.dp)
            .width(175.dp)
    ) {
        GlideImage(data?.thumbnail, data?.name, Modifier.fillMaxWidth())
        Spacer(Modifier.height(9.dp))
        TextViewNormal(data?.name ?: "", 16, line = 1)
        Box(Modifier.offset(y= (-2).dp)) {
            TextViewLight(data?.artists ?: "", 14, line = 1)
        }
    }
}