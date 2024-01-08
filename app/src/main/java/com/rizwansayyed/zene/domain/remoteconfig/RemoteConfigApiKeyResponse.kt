package com.rizwansayyed.zene.domain.remoteconfig

data class RemoteConfigApiKeyResponse(
    val yt: String,
    val music: String,
    val soundcloud: String,
    val spotifyClientID: String,
    val spotifySecretID: String,
    val giphy: String,
    val instagram: String,
)