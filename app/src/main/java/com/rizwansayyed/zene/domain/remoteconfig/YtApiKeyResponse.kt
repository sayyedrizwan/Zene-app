package com.rizwansayyed.zene.domain.remoteconfig

data class YtApiKeyResponse(
    val yt: String,
    val music: String,
    val soundcloud: String,
    val spotifyClientID: String,
    val spotifySecretID: String,
)