package com.rizwansayyed.zene.domain.spotify.music

data class SpotifyAccessTokenResponse(
    val access_token: String?,
    val expires_in: Int?,
    val token_type: String?
)