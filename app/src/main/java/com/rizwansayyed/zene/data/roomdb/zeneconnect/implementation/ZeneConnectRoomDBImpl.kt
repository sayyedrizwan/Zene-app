package com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation

import androidx.lifecycle.LiveData
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.ZeneConnectContactDatabase
import com.rizwansayyed.zene.data.roomdb.zeneconnect.ZeneConnectVibesDatabase
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ZeneConnectRoomDBImpl @Inject constructor(
    private val contactDB: ZeneConnectContactDatabase,
    private val vibesDB: ZeneConnectVibesDatabase,
) : ZeneConnectRoomDBInterface {

    override suspend fun get(): LiveData<List<ZeneConnectContactsModel>> {
        return contactDB.contactsDao().get()
    }

    override suspend fun getList() = flow {
        emit(contactDB.contactsDao().getList())
    }.flowOn(Dispatchers.IO)

    override suspend fun newPostsCounts(number: String) = flow {
        emit(vibesDB.vibesDao().newPosts(number))
    }.flowOn(Dispatchers.IO)

    override suspend fun postsCounts(number: String) = flow {
        emit(vibesDB.vibesDao().allPostsNumber(number))
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllVibes(number: String) = flow {
        emit(vibesDB.vibesDao().allVibesNumberSortNew(number))
    }.flowOn(Dispatchers.IO)


    override suspend fun insert(
        list: List<ZeneUsersResponse>, contacts: ArrayList<ContactListData>, phoneNumberCode: String
    ) = flow {
        val l = list.map {
            val name = contacts.find { c ->
                c.number.contains(it.phone_number!!.replace("+${phoneNumberCode}", ""))
            }
            ZeneConnectContactsModel(
                it.phone_number ?: "",
                it.email,
                it.profile_photo,
                name?.name,
                it.song_name,
                it.song_artists,
                it.song_id,
                it.song_thumbnail,
                0,
                true,
                System.currentTimeMillis()
            )
        }
        l.forEach { contactDB.contactsDao().insert(it) }
        emit(null)
    }.flowOn(Dispatchers.IO)

}