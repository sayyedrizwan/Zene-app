package com.rizwansayyed.zene.domain.remoteconfig

import com.rizwansayyed.zene.data.utils.moshi

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


fun RemoteConfigZeneAdsResponse.toCache(): String? {
    return moshi.adapter(RemoteConfigZeneAdsResponse::class.java).toJson(this)
}
