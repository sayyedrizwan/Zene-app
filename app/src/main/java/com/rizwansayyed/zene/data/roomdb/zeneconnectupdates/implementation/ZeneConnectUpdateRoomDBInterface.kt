package com.rizwansayyed.zene.data.roomdb.zeneconnectupdates.implementation

import androidx.lifecycle.LiveData
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectVibesModel
import com.rizwansayyed.zene.data.roomdb.zeneconnectupdates.model.ZeneConnectUpdatesModel
import kotlinx.coroutines.flow.Flow

interface ZeneConnectUpdateRoomDBInterface {

    suspend fun getAll(): Flow<List<ZeneConnectUpdatesModel>>
}