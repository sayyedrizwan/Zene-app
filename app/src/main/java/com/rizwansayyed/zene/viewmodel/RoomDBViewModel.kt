package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.roomdb.offlinesongs.implementation.OfflineSongsDBInterface
import com.rizwansayyed.zene.data.roomdb.updates.implementation.UpdatesRoomDBInterface
import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation.ZeneConnectRoomDBInterface
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectVibesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoomDBViewModel @Inject constructor(
    private val updateDB: UpdatesRoomDBInterface,
    private val offlineSongsDB: OfflineSongsDBInterface,
    private val zeneConnectDB: ZeneConnectRoomDBInterface,
) : ViewModel() {

    var updateLists = mutableStateListOf<UpdateData>()
    var contactsLists = mutableStateListOf<ZeneConnectContactsModel>()
    var vibesListsData by mutableStateOf<APIResponse<List<ZeneConnectVibesModel>>>(APIResponse.Empty)
    var isSaved by mutableStateOf(false)
    var isLoading by mutableStateOf(true)
    var doShowMoreLoading by mutableStateOf(false)

    fun updateLists(p: Int, address: String) = viewModelScope.launch(Dispatchers.IO) {
        if (p == 0) updateLists.clear()

        updateDB.getLists(address, p).onStart {
            isLoading = true
            doShowMoreLoading = false
        }.catch {
            isLoading = false
            doShowMoreLoading = false
        }.collectLatest {
            isLoading = false
            doShowMoreLoading = it.size >= 30
            updateLists.addAll(it)
        }
    }

    fun removeUpdatesLists(position: Int, u: UpdateData) = viewModelScope.launch(Dispatchers.IO) {
        updateLists.removeAt(position)
        updateDB.remove(u).catch { }.collectLatest { }
    }

    fun removeAllEarphones(address: String) = viewModelScope.launch(Dispatchers.IO) {
        updateDB.removeAll(address).catch { }.collectLatest { }
    }

    fun saveOfflineSongs(m: ZeneMusicDataItems) = viewModelScope.launch(Dispatchers.IO) {
        offlineSongsDB.save(m).catch { }.collectLatest { }
    }

    fun isSongSaved(m: ZeneMusicDataItems) = viewModelScope.launch(Dispatchers.IO) {
        delay(500)
        offlineSongsDB.isSaved(m.id ?: "").catch { }.collectLatest {
            isSaved = it > 0
        }
    }

    fun removeOfflineSong(m: ZeneMusicDataItems) = viewModelScope.launch(Dispatchers.IO) {
        offlineSongsDB.delete(m.id ?: "").catch { }.collectLatest {}
    }

    fun getAllContacts() = viewModelScope.launch(Dispatchers.Main) {
        zeneConnectDB.getList().catch { }.collectLatest {
            contactsLists.clear()
            it.forEach { c ->
                viewModelScope.launch(Dispatchers.IO) {
                    val i = zeneConnectDB.newPostsCounts(c.number).firstOrNull() ?: 0
                    if (i > 0) {
                        c.numberOfPosts = i
                        c.isNew = true
                    } else {
                        val num = zeneConnectDB.postsCounts(c.number).firstOrNull() ?: 0
                        c.numberOfPosts = num
                        c.isNew = false
                    }
                    if (!contactsLists.any { it.number == c.number }) contactsLists.add(c)

                    if (isActive) cancel()
                }
            }
        }
    }


    fun contactsAllVibes(number: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneConnectDB.getAllVibes(number).onStart {
            vibesListsData = APIResponse.Loading
        }.catch {
            vibesListsData = APIResponse.Error(it)
        }.collectLatest {
            vibesListsData = APIResponse.Success(it)
        }
    }


    fun resetNewVibes(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        zeneConnectDB.resetNewVibes(id).onStart { }.catch { }.collectLatest { }
    }

}