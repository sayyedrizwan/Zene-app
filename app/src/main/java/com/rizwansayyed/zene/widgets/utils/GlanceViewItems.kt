package com.rizwansayyed.zene.widgets.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GlanceImage(img: String?, size: Int?, c: Context, h: Int = 5) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val coroutine = rememberCoroutineScope()

    if (bitmap != null) Image(
        provider = ImageProvider(bitmap!!),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = if (size == null) GlanceModifier.padding(horizontal = h.dp).fillMaxSize()
        else GlanceModifier.padding(horizontal = h.dp).size(size.dp).cornerRadius(13.dp)
    )

    LaunchedEffect(img) {
        coroutine.launch(Dispatchers.IO) {
            if (img != null) {
                try {
                    val b = Glide.with(c).asBitmap().load(img).submit(200, 200).get()
                    bitmap = b
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

@Composable
fun GlanceImageIcon(icon: Int, size: Int, click: () -> Unit) {
    Image(provider = ImageProvider(icon),
        contentDescription = null,
        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
        modifier = GlanceModifier.size(size.dp).clickable { click() })
}

@Composable
fun GlanceImageIcon(icon: Int, size: Int, tint: Color? = Color.Black) {
    Image(
        provider = ImageProvider(icon),
        contentDescription = null,
        colorFilter = if (tint == null) null else ColorFilter.tint(ColorProvider(tint)),
        modifier = GlanceModifier.size(size.dp)
    )
}


@Composable
fun GlanceTextItemBold(text: String?, size: Int = 16, max: Int = 1) {
    Text(
        text ?: "", GlanceModifier, TextStyle(
            color = ColorProvider(Color.White),
            fontSize = size.sp,
            FontWeight.Bold,
        ), max
    )
}

@Composable
fun GlanceTextItemNormal(text: String?, size: Int = 12, max: Int = 1) {
    Text(
        text ?: "", GlanceModifier, TextStyle(
            color = ColorProvider(Color.White), fontSize = size.sp, FontWeight.Normal
        ), max
    )
}