package com.rizwansayyed.zene.data.api.model


typealias ZeneMusicHistoryResponse = List<Root2>

data class Root2(
    val _id: String,
    val email: String,
    val name: String,
    val artists: String,
    val id: String,
    val thumbnail: String,
    val deviceInfo: String,
    val deviceType: String,
    val timestamp: Long,
    val timesItsPlayed: Long,
)