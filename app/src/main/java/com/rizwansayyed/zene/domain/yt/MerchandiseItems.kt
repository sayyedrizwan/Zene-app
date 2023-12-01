package com.rizwansayyed.zene.domain.yt


data class MerchandiseItems(
    val description: String?,
    val title: String?,
    val thumbnail: String?,
    val price: String?,
    val link: String?,
)


data class RetryItems(
    var extra: String?,
    var retry: Int?,
)

