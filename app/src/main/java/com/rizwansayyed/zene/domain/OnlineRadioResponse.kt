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
    return MusicData(i, name, language, serveruuid, MusicType.RADIO, url_resolved)
}

data class OnlineRadioResponseItem(
    val bitrate: Int? = null,
    val changeuuid: String? = null,
    val clickcount: Int? = null,
    val clicktimestamp: String? = null,
    val clicktimestamp_iso8601: String? = null,
    val clicktrend: Int? = null,
    val codec: String? = null,
    val country: String? = null,
    val countrycode: String? = null,
    val favicon: String? = null,
    val geo_lat: Double? = null,
    val geo_long: Double? = null,
    val has_extended_info: Boolean? = null,
    val hls: Int? = null,
    val homepage: String? = null,
    val iso_3166_2: Any? = null,
    val language: String? = null,
    val languagecodes: String? = null,
    val lastchangetime: String? = null,
    val lastchangetime_iso8601: String? = null,
    val lastcheckok: Int? = null,
    val lastcheckoktime: String? = null,
    val lastcheckoktime_iso8601: String? = null,
    val lastchecktime: String? = null,
    val lastchecktime_iso8601: String? = null,
    val lastlocalchecktime: String? = null,
    val lastlocalchecktime_iso8601: Any? = null,
    val name: String? = null,
    val serveruuid: String? = null,
    val ssl_error: Int? = null,
    val state: String? = null,
    val stationuuid: String? = null,
    val tags: String? = null,
    val url: String? = null,
    val url_resolved: String? = null,
    val votes: Int? = null
)