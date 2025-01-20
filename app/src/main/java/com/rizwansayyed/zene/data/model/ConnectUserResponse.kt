package com.rizwansayyed.zene.data.model

import com.rizwansayyed.zene.utils.ContactData

data class ConnectUserResponse(
    val email: String?,
    val name: String?,
    val profile_photo: String?,
    val last_seen: String?,
    val connect_status: String?
)


data class ConnectUsersResponse(
    val users: List<ConnectUserResponse>?,
    val contacts: List<ContactData>?,
)