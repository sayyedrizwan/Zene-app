package com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation

import androidx.lifecycle.LiveData
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.ZeneConnectContactDatabase
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ZeneConnectRoomDBImpl @Inject constructor(
    private val contactDB: ZeneConnectContactDatabase
) : ZeneConnectRoomDBInterface {

    override suspend fun get(): LiveData<List<ZeneConnectContactsModel>> {
       return contactDB.contactsDao().get()
    }


    override suspend fun insert(
        list: List<ZeneUsersResponse>, contacts: ArrayList<ContactListData>, phoneNumberCode: String
    ) = flow {
        val l = list.map {
            val name = contacts.find { c ->
                c.number.contains(it.phone_number!!.replace("+${phoneNumberCode}", ""))
            }
            ZeneConnectContactsModel(
                null, it.phone_number ?: "", it.email, it.profile_photo, name?.name,
                it.song_name, it.song_artists, it.song_id, it.song_thumbnail,
                0, System.currentTimeMillis()
            )
        }
        l.forEach { contactDB.contactsDao().insert(it) }
        emit(null)
    }.flowOn(Dispatchers.IO)

}