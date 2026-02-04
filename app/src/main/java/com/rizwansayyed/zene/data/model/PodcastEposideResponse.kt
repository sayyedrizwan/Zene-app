package com.rizwansayyed.zene.data.model

import android.os.Build
import android.text.Html

data class PodcastEpisodeResponse(
    val description: String?,
    val home: String?,
    val id: String?,
    val image: Image?,
    val lookup: String?,
    val series: Series?,
    val share: String?,
    val size: Int?,
    val slug: String?,
    val title: String?
) {
    data class Image(
        val type: String?,
        val url: String?
    )

    data class Series(
        val description: String?,
        val home: String?,
        val id: Int?,
        val slug: String?,
        val image: Image?,
        val imageURL: String?,
        val title: String?,
        val type: String?,
        val updatedAt: Int?,
        val url: String?
    ) {

        data class Image(
            val id: Int?,
            val palette: List<String?>?,
            val suffix: String?,
            val type: String?,
            val url: String?,
            val urlBase: String?
        )
    }

    @Suppress("DEPRECATION")
    fun getAsMusicData(): ZeneMusicData {
        val artists = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(description).toString()
        }
        val t = image?.url ?: series?.imageURL
        return ZeneMusicData(
            artists,
            slug.toString(),
            title,
            lookup,
            t,
            MusicDataTypes.PODCAST_AUDIO.name
        )
    }
}