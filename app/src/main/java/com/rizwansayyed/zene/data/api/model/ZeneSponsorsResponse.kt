package com.rizwansayyed.zene.data.api.model


data class ZeneSponsorsItems(
    val ads: Ads?,
    val showAds: Boolean?,
    val showSponserAds: Boolean?
) {
    data class Ads(
        val bottom: Bottom?,
        val top: Top?
    ) {
        data class Bottom(
            val media: String?,
            val link: String?,
            val title: String?
        )

        data class Top(
            val imgs: List<Img?>?,
            val title: String?
        ) {
            data class Img(
                val img: String?,
                val link: String?
            )
        }
    }
}

data class ZeneSponsorsResponse(
    val android: ZeneSponsorsItems?,
    val ios: ZeneSponsorsItems?,
    val web: ZeneSponsorsItems?
)