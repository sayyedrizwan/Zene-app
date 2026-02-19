package com.rizwansayyed.zene.ui.main.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.core.graphics.toColorInt
import com.rizwansayyed.zene.utils.share.MediaContentUtils


@Composable
fun HomeTopHeaderView(headerText: MutableList<AlertShowcaseItem>) {
    if (headerText.isNotEmpty()) FlowRow(
        modifier = Modifier.fillMaxWidth().padding(9.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        headerText.sortedBy { it.id }.forEach { item ->
            Box(Modifier.clickable(enabled = item.redirect_url != null) {
                MediaContentUtils.openCustomBrowser(item.redirect_url)
            }) {
                if (item.bold) TextViewBold(
                    item.text,
                    color = item.color?.let { Color(it.toColorInt()) } ?: Color.White)
                else TextViewNormal(
                    item.text,
                    color = item.color?.let { Color(it.toColorInt()) } ?: Color.White)
            }
        }
    }
}

fun topHeaderAlert(): List<AlertShowcaseItem> {
    val url = BuildConfig.ZENE_TOP_ALERT_DOCS
    val client = OkHttpClient().newBuilder().build()
    try {
        val request = Request.Builder().url(url).get().build()
        val response = client.newCall(request).execute()

        val body = response.body.string()
        val json = moshi.adapter(AlertShowcaseResponse::class.java).fromJson(body)

        if (!(json?.show ?: false)) return emptyList()
        return json.showcase_items ?: emptyList()
    } catch (_: Exception) {
        return emptyList()
    }
}

data class AlertShowcaseResponse(
    val show: Boolean?, val showcase_items: List<AlertShowcaseItem>?
)

data class AlertShowcaseItem(
    val id: Int?,
    val text: String,
    val color: String? = null,
    val redirect_url: String? = null,
    val bold: Boolean
)
