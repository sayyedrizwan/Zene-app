package com.rizwansayyed.zene.ui.main.ent.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.rizwansayyed.zene.ui.theme.LuxColor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URI

object BrandCache {
    val brandData = mutableMapOf<String, BrandInfo>()

    fun extractBrandName(url: String?): String {
        val host = URI(url).host ?: return ""
        val parts = host.removePrefix("www.").split(".")
        return if (parts.size >= 2) {
            parts[parts.size - 2].uppercase()
        } else {
            parts.first().uppercase()
        }
    }

    private val httpClient = OkHttpClient()

    fun downloadFavicon(faviconUrl: String): Bitmap? {
        return try {
            val request = Request.Builder().url(faviconUrl).build()
            val response = httpClient.newCall(request).execute()
            BitmapFactory.decodeStream(response.body.byteStream())
        } catch (e: Exception) {
            null
        }
    }

    fun extractDarkColor(bitmap: Bitmap): Color {
        val palette = Palette.from(bitmap)
            .clearFilters()
            .generate()

        val colorInt =
            palette.darkVibrantSwatch?.rgb
                ?: palette.darkMutedSwatch?.rgb
                ?: palette.dominantSwatch?.rgb
                ?: LuxColor.hashCode()

        return Color(colorInt)
    }

}

data class BrandInfo(
    val name: String,
    val darkColor: Color
)
