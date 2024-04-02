package com.rizwansayyed.zene.domain.remoteconfig

data class RemoteConfigZeneAdsResponse(
    val doShow: Boolean?,
    val homeGridTop: List<HomeGridTop?>?,
    val homeMiddleView: HomeMiddleView?,
    val title: String?
) {
    data class HomeGridTop(
        val img: String?,
        val link: String?
    )

    data class HomeMiddleView(
        val img: String?,
        val link: String?
    )
}