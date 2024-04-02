package com.rizwansayyed.zene.domain.remoteconfig

import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache

data class RemoteConfigApiKeyResponse(
    val yt: String,
    val music: String,
    val soundcloud: String,
    val spotifyClientID: String,
    val spotifySecretID: String,
    val giphy: String,
    val instagram: String,
)


fun RemoteConfigApiKeyResponse.toCache(): String? {
    return moshi.adapter(RemoteConfigApiKeyResponse::class.java).toJson(this)
}
