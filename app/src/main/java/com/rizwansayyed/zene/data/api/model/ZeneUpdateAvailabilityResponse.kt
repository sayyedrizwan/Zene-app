package com.rizwansayyed.zene.data.api.model

data class ZeneUpdateAvailabilityResponse(
    val android: ZeneUpdateAvailabilityItem?,
)

data class ZeneUpdateAvailabilityItem(
    val appVersion: String?
)