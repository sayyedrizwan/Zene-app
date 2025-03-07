package com.rizwansayyed.zene.widgets.utils

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
fun GlanceImage(img: Bitmap, size: Int?, h: Int = 5) {
    Image(
        provider = ImageProvider(img),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = if (size == null) GlanceModifier.padding(horizontal = h.dp).fillMaxSize()
        else GlanceModifier.padding(horizontal = h.dp).size(size.dp).cornerRadius(13.dp)
    )
}

@Composable
fun GlanceImageIcon(icon: Int, size: Int, click: () -> Unit) {
    Image(provider = ImageProvider(icon),
        contentDescription = null,
        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
        modifier = GlanceModifier.size(size.dp).clickable { click() })
}

@Composable
fun GlanceImageIcon(icon: Int, size: Int, tint: Color = Color.Black) {
    Image(
        provider = ImageProvider(icon),
        contentDescription = null,
        colorFilter = ColorFilter.tint(ColorProvider(tint)),
        modifier = GlanceModifier.size(size.dp)
    )
}


@Composable
fun GlanceTextItemBold(text: String?) {
    Text(
        text ?: "", GlanceModifier, TextStyle(
            color = ColorProvider(Color.White),
            fontSize = 16.sp,
            FontWeight.Bold,
        ), 1
    )
}

@Composable
fun GlanceTextItemNormal(text: String?) {
    Text(
        text ?: "", GlanceModifier, TextStyle(
            color = ColorProvider(Color.White), fontSize = 12.sp, FontWeight.Normal
        ), 1
    )
}