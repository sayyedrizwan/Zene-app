package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.data.utils.moshi

typealias OnlineRadioResponse = List<OnlineRadioResponseItem>

data class OnlineRadioCacheResponse(
    val cacheTime: Long,
    val list: List<OnlineRadioResponseItem>
)

fun OnlineRadioResponse.toTxtCache(): String? {
    val r = OnlineRadioCacheResponse(System.currentTimeMillis(), this)
    return moshi.adapter(r.javaClass).toJson(r)
}

fun OnlineRadioResponseItem.toMusicData(): MusicData {
    val i =
        favicon?.ifEmpty { "https://cdn-icons-png.flaticon.com/512/7999/7999266.png" }
    return MusicData(i, name, language, serveruuid, MusicType.RADIO)
}

data class OnlineRadioResponseItem(
    val bitrate: Int?,
    val changeuuid: String?,
    val clickcount: Int?,
    val clicktimestamp: String?,
    val clicktimestamp_iso8601: String?,
    val clicktrend: Int?,
    val codec: String?,
    val country: String?,
    val countrycode: String?,
    val favicon: String?,
    val geo_lat: Double?,
    val geo_long: Double?,
    val has_extended_info: Boolean?,
    val hls: Int?,
    val homepage: String?,
    val iso_3166_2: Any?,
    val language: String?,
    val languagecodes: String?,
    val lastchangetime: String?,
    val lastchangetime_iso8601: String?,
    val lastcheckok: Int?,
    val lastcheckoktime: String?,
    val lastcheckoktime_iso8601: String?,
    val lastchecktime: String?,
    val lastchecktime_iso8601: String?,
    val lastlocalchecktime: String?,
    val lastlocalchecktime_iso8601: Any?,
    val name: String?,
    val serveruuid: String?,
    val ssl_error: Int?,
    val state: String?,
    val stationuuid: String?,
    val tags: String?,
    val url: String?,
    val url_resolved: String?,
    val votes: Int?
)