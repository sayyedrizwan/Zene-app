package com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation

import androidx.lifecycle.LiveData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.ZeneConnectContactDatabase
import com.rizwansayyed.zene.data.roomdb.zeneconnect.ZeneConnectVibesDatabase
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectVibesModel
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.saveConnectImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class ZeneConnectRoomDBImpl @Inject constructor(
    private val zeneAPI: ZeneAPIService,
    private val contactDB: ZeneConnectContactDatabase,
    private val vibesDB: ZeneConnectVibesDatabase,
    private val zeneConnectDB: ZeneConnectVibesDatabase
) : ZeneConnectRoomDBInterface {

    override suspend fun get(): LiveData<List<ZeneConnectContactsModel>> {
        return contactDB.contactsDao().get()
    }

    override suspend fun getList() = flow {
        emit(contactDB.contactsDao().getList())
    }.flowOn(Dispatchers.IO)

    override suspend fun getPosts(id: Int) = flow {
        emit(vibesDB.vibesDao().getPosts(id))
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

    override suspend fun resetNewVibes(id: Int) = flow {
        emit(vibesDB.vibesDao().resetNewVibes(id))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateSeenVibes(number: String, photo: String) = flow {
        emit(vibesDB.vibesDao().updateSeenVibes(number, photo))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateEmojiVibes(number: String, photo: String, emoji: String) = flow {
        emit(vibesDB.vibesDao().updateEmojiVibes(number, photo, emoji))
    }.flowOn(Dispatchers.IO)


    override suspend fun insert(
        list: List<ZeneUsersResponse>, contacts: ArrayList<ContactListData>, phoneNumberCode: String
    ) = flow {
        val num = userInfoDB.firstOrNull()?.phonenumber ?: return@flow
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
        l.forEach { if (it.number != num)  contactDB.contactsDao().insert(it) }
        emit(null)
    }.flowOn(Dispatchers.IO)


    override suspend fun sendConnectVibes(
        connect: ZeneConnectContactsModel, song: ZeneMusicDataItems?
    ) = flow {
        val requestFile = saveConnectImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val fileForm = MultipartBody.Part.createFormData("file", saveConnectImage.name, requestFile)
        val pn = (userInfoDB.firstOrNull()?.phonenumber?.trim()
            ?: "").toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val toPN = connect.number.trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val songJson = moshi.adapter(ZeneMusicDataItems::class.java).toJson(song)
        val songData = songJson.trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        emit(zeneAPI.sendConnectVibes(fileForm, pn, toPN, songData))
    }.flowOn(Dispatchers.IO)


    override suspend fun getVibes() = flow {
        val number = (userInfoDB.firstOrNull()?.phonenumber?.trim() ?: "")
        val json = JSONObject().apply {
            put("number", number)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val list = zeneAPI.getConnectVibes(body)
        list.forEach {
            it.from_number?.let { num ->
                val insert = ZeneConnectVibesModel(
                    null,
                    num,
                    it.timestamp,
                    it.image_path,
                    it.songid,
                    it.atists,
                    it.thumbnail,
                    it.name,
                    it.type,
                    true
                )

                zeneConnectDB.vibesDao().insert(insert)
            }
        }
        emit(Unit)
    }.flowOn(Dispatchers.IO)


    override suspend fun seenVibes(toNumber: String, photoURL: String) = flow {
        val number = (userInfoDB.firstOrNull()?.phonenumber?.trim() ?: "")
        val json = JSONObject().apply {
            put("fromnumber", number)
            put("tonumber", toNumber)
            put("photo", photoURL)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.seenVibes(body))
    }.flowOn(Dispatchers.IO)


    override suspend fun reactToVibes(toNumber: String, photoURL: String, emoji: String) = flow {
        val number = (userInfoDB.firstOrNull()?.phonenumber?.trim() ?: "")
        val json = JSONObject().apply {
            put("fromnumber", number)
            put("tonumber", toNumber)
            put("photo", photoURL)
            put("emoji", emoji)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.reactToVibes(body))
    }.flowOn(Dispatchers.IO)
}