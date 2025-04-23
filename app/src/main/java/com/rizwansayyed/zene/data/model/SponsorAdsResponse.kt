package com.rizwansayyed.zene.data.model

data class SponsorAdsResponse(
    val android: AndroidSponsorAds
)

data class AndroidSponsorAds(
    val top: TopSponsorAds,
    val bottom: TopSponsorAds,
    val entertainment: TopSponsorAds
)

data class TopSponsorAds(
    val title: String?,
    val media: List<String>?,
    val link: String?,
    val button: String?,
)