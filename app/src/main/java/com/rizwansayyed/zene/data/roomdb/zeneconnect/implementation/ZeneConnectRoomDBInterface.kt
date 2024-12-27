package com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation

import androidx.lifecycle.LiveData
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectVibesModel
import kotlinx.coroutines.flow.Flow

interface ZeneConnectRoomDBInterface {

    suspend fun insert(
        list: List<ZeneUsersResponse>, contacts: ArrayList<ContactListData>, phoneNumberCode: String
    ): Flow<Nothing?>

    suspend fun get(): LiveData<List<ZeneConnectContactsModel>>
    suspend fun newPostsCounts(number: String): Flow<Int>
    suspend fun postsCounts(number: String): Flow<Int>
    suspend fun getList(): Flow<List<ZeneConnectContactsModel>>
    suspend fun getAllVibes(number: String): Flow<List<ZeneConnectVibesModel>>
    suspend fun resetNewVibes(id: Int): Flow<Unit>
    suspend fun getPosts(id: Int): Flow<ZeneConnectVibesModel?>
    suspend fun reactToVibes(
        toNumber: String,
        photoURL: String,
        emoji: String
    ): Flow<ZeneBooleanResponse>

    suspend fun seenVibes(toNumber: String, photoURL: String): Flow<ZeneBooleanResponse>
    suspend fun getVibes(): Flow<Unit>
    suspend fun sendConnectVibes(
        connect: ZeneConnectContactsModel,
        song: ZeneMusicDataItems?
    ): Flow<ZeneBooleanResponse>

    suspend fun updateSeenVibes(number: String, photo: String): Flow<Unit>
    suspend fun updateEmojiVibes(number: String, photo: String, emoji: String): Flow<Unit>
}