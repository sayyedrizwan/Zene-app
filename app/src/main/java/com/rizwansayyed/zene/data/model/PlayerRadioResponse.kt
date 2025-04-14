package com.rizwansayyed.zene.data.model

data class PlayerRadioResponse(
    val changeuuid: String?,
    val country: String?,
    val countrycode: String?,
    val favicon: String?,
    val geo_lat: Any?,
    val geo_long: Any?,
    val homepage: String?,
    val iso_3166_2: String?,
    val language: String?,
    val languagecodes: String?,
    val lastchangetime: String?,
    val lastchangetime_iso8601: String?,
    val name: String?,
    val state: String?,
    val stationuuid: String?,
    val tags: String?,
    val url: String?,
    val votes: Int?
) {
    fun toMusicData(): ZeneMusicData {
        val artists = if (tags == "") language else tags
        val thumbnail =
            if (favicon === "") "https://www.zenemusic.co/zene_fm_thumbnail.png" else favicon
        return ZeneMusicData(artists, stationuuid, name, url, thumbnail, MusicDataTypes.RADIO.name)
    }
}