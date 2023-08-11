package com.rizwansayyed.zene.presenter.model

data class IpJSONResponse(
    val `as`: String?,
    val city: String?,
    val country: String?,
    val countryCode: String?,
    val isp: String?,
    val lat: Double?,
    val lon: Double?,
    val org: String?,
    val query: String?,
    val region: String?,
    val regionName: String?,
    val status: String?,
    val timezone: String?,
    val zip: String?
)