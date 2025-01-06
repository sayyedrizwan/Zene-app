package com.rizwansayyed.zene.data.model

data class RadioDataResponse(
    val recent: List<ZeneMusicData?>?,
    val countryRadio: List<ZeneMusicData?>?,
    val recommendedRadio: List<ZeneMusicData?>?,
    val exploreRadio: List<ZeneMusicData?>?,
)