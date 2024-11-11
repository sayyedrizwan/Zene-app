package com.rizwansayyed.zene.data.api.model

data class TrueCallerResponse(
    val access_token: String,
    val expires_in: String,
    val scope: String,
    val token_type: String,
    val id_token: String
)

data class TrueCallerUserInfoResponse(
    val sub: String,
    val phone_number: String,
    val phone_number_verified: Boolean,
    val phone_number_country_code: String
)