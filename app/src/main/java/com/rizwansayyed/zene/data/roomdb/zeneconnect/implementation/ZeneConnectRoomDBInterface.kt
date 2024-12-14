package com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation

import androidx.lifecycle.LiveData
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import kotlinx.coroutines.flow.Flow

interface ZeneConnectRoomDBInterface {

    suspend fun insert(
        list: List<ZeneUsersResponse>, contacts: ArrayList<ContactListData>, phoneNumberCode: String
    ): Flow<Nothing?>

    suspend fun get(): Flow<Flow<List<ZeneConnectContactsModel>>>
}