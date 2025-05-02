package com.rizwansayyed.zene.ui.main.home

import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import okhttp3.OkHttpClient
import okhttp3.Request


@Composable
fun HomeTopHeaderView(headerText: String) {
    if (headerText.trim().length > 2) AndroidView(factory = { context ->
        TextView(context).apply {
            movementMethod = LinkMovementMethod.getInstance()
            setTextColor(Color.White.toArgb())
            linksClickable = true
            gravity = Gravity.CENTER
            textSize = 15f
            setTextIsSelectable(false)
            setSingleLine(false)
        }
    }, Modifier
        .fillMaxWidth()
        .wrapContentHeight(), {
        it.text = HtmlCompat.fromHtml(headerText, HtmlCompat.FROM_HTML_MODE_COMPACT)
    })
}

fun topHeaderAlert(): String {
    val url = "https://docs.google.com/document/d/1DvYmmvIkIebo5TXTf826McrpJM8frAXqwPi2l0RCBsA/edit"
    val client = OkHttpClient().newBuilder().build()
    try {
        val request = Request.Builder().url(url).get().build()
        val response = client.newCall(request).execute()

        val body = response.body?.string()

        if (body?.contains("DOCS_modelChunk = [{\"ty\":\"is\",\"ibi\":1") == false) return ""
        val value = body?.substringAfter("DOCS_modelChunk = [{\"ty\":\"is\",\"ibi\":1")
            ?.substringAfter(":\"")?.substringBefore("\"},{")?.trim() ?: ""

        val decodedString = HtmlCompat.fromHtml(value, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        val unescaped =
            decodedString.replace("\\u003c", "<").replace("\\u003e", ">").replace("\\u003d", "=")
                .replace("\\\"", "\"")
        return unescaped
    } catch (_: Exception) {
        return ""
    }
}