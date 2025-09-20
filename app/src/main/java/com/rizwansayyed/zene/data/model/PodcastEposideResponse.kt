package com.rizwansayyed.zene.data.model

import android.os.Build
import android.text.Html

data class PodcastEpisodeResponse(
    val description: String?,
    val duration: Int?,
    val explicit: Boolean?,
    val home: String?,
    val id: Int?,
    val image: Image?,
    val lookup: String?,
    val mediaType: String?,
    val publishedAt: Int?,
    val rawTitle: String?,
    val series: Series?,
    val share: String?,
    val size: Int?,
    val slug: String?,
    val title: String?,
    val type: String?,
    val url: String?,
    val versionInfo: String?
) {
    data class Image(
        val type: String?,
        val url: String?
    )

    data class Series(
        val access: String?,
        val author: String?,
        val backgroundColor: String?,
        val color: List<Int?>?,
        val currentURL: String?,
        val description: String?,
        val descriptionFingerprint: String?,
        val fetch: Fetch?,
        val fetchStatus: String?,
        val fingerprint: String?,
        val home: String?,
        val id: Int?,
        val image: Image?,
        val imageURL: String?,
        val language: String?,
        val latestLookup: String?,
        val lookup: String?,
        val mediaKind: String?,
        val network: Network?,
        val networkRecord: Any?,
        val relatedLookup: String?,
        val share: String?,
        val slug: String?,
        val stats: Stats?,
        val subtitle: String?,
        val tags: List<Tag?>?,
        val title: String?,
        val type: String?,
        val updatedAt: Int?,
        val url: String?
    ) {
        data class Fetch(
            val confidence: Int?,
            val status: String?
        )

        data class Image(
            val id: Int?,
            val palette: List<String?>?,
            val suffix: String?,
            val type: String?,
            val url: String?,
            val urlBase: String?
        )

        data class Network(
            val name: String?
        )

        data class Stats(
            val averageDuration: Int?,
            val averageInterval: Int?,
            val earliestPublishedAt: Int?,
            val latestPublishedAt: Int?,
            val longTrendCentile: Double?,
            val manualSubscriptionsCentile: Double?,
            val numberOfEpisodes: Int?,
            val numberOfSubscriptions: Int?,
            val shortTrendCentile: Double?
        )

        data class Tag(
            val ancestors: List<String?>?,
            val id: Int?,
            val language: String?,
            val polar: Double?,
            val rawTitle: String?,
            val series: Series?,
            val sources: List<String?>?,
            val title: String?,
            val topic: Topic?,
            val type: String?
        ) {
            data class Series(
                val amount: Int?,
                val centile: Double?
            )

            data class Topic(
                val id: Int?,
                val owner: Owner?,
                val title: String?
            ) {
                data class Owner(
                    val id: Int?
                )
            }
        }
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
            id.toString(),
            title,
            lookup,
            t,
            MusicDataTypes.PODCAST_AUDIO.name
        )
    }
}