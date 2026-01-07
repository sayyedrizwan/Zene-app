package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.ent.utils.BrandCache
import com.rizwansayyed.zene.ui.main.ent.utils.BrandCache.downloadFavicon
import com.rizwansayyed.zene.ui.main.ent.utils.BrandCache.extractBrandName
import com.rizwansayyed.zene.ui.main.ent.utils.BrandCache.extractDarkColor
import com.rizwansayyed.zene.ui.main.ent.utils.BrandInfo
import com.rizwansayyed.zene.ui.theme.LuxColor
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.URLSUtils.getSearchNewsOnGoogle
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntBuzzNewsViewItem(item: ZeneMusicData) {
    var webColor by remember { mutableStateOf<BrandInfo?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 19.dp)
            .clickable {
                MediaContentUtils.openCustomBrowser(item.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CategoryChip(extractBrandName(item.id), webColor?.darkColor)
                Spacer(modifier = Modifier.width(8.dp))
                TextViewNormal("${item.extra} ${stringResource(R.string.ago)}", size = 13)
            }

            Spacer(modifier = Modifier.height(6.dp))
            TextViewBold(item.name ?: "", size = 15)
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        GlideImage(
            item.thumbnail, "",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
    }

    LaunchedEffect(item.id) {
        BrandCache.brandData[item.id]?.let {
            webColor = it
            return@LaunchedEffect
        }

        val brandInfo = withContext(Dispatchers.IO) {
            val brandName = extractBrandName(item.id ?: "")
            val faviconUrl = "https://${URI(item.id ?: "").host}/favicon.ico"

            val bitmap = downloadFavicon(faviconUrl)
            val darkColor = bitmap?.let { extractDarkColor(it) } ?: Color.DarkGray
            BrandInfo(brandName, darkColor)
        }

        BrandCache.brandData[item.id ?: ""] = brandInfo
        webColor = brandInfo
    }
}

@Composable
fun CategoryChip(text: String, color: Color?) {
    Box(
        modifier = Modifier
            .background(
                color?.copy(alpha = 0.25f) ?: LuxColor, RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        TextViewSemiBold(text, 14, Color.White)
    }
}

@Composable
fun ViewAllButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 19.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MainColor)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextViewBold(stringResource(R.string.view_all_buzz), size = 16)

            Spacer(modifier = Modifier.width(6.dp))

            ImageIcon(R.drawable.ic_arrow_right, 18, Color.White)
        }
    }
}
