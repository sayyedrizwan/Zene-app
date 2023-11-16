package com.rizwansayyed.zene.domain.news

data class GoogleNewsResponse(
    val rss: Rss?
) {
    data class Rss(
        val `_version`: String?,
//        val `_xmlns:media`: String?,
        val channel: Channel?
    ) {
        data class Channel(
            val copyright: String?,
            val description: String?,
            val generator: String?,
            val item: List<Item?>?,
            val language: String?,
            val lastBuildDate: String?,
            val link: String?,
            val title: String?,
            val webMaster: String?
        ) {
            data class Item(
                val description: String?,
                val guid: Guid?,
                val link: String?,
                val pubDate: String?,
                val source: Source?,
                val title: String?
            ) {
                data class Guid(
                    val __text: String?,
                    val _isPermaLink: String?
                )

                data class Source(
                    val __text: String?,
                    val _url: String?
                )
            }
        }
    }
}