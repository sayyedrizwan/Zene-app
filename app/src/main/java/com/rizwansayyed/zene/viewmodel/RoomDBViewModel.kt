package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.roomdb.offlinesongs.implementation.OfflineSongsDBInterface
import com.rizwansayyed.zene.data.roomdb.offlinesongs.model.OfflineSongsData
import com.rizwansayyed.zene.data.roomdb.updates.implementation.UpdatesRoomDBInterface
import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoomDBViewModel @Inject constructor(
    private val updateDB: UpdatesRoomDBInterface,
    private val offlineSongsDB: OfflineSongsDBInterface,
) : ViewModel() {

    var offlineSongsLists = mutableStateListOf<OfflineSongsData>()
    var updateLists = mutableStateListOf<UpdateData>()
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
        offlineSongsDB.isSaved(m.id ?: "").catch { }.collectLatest {
            isSaved = it > 0
        }
    }

    fun offlineSongsLists(p: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (p == 0) offlineSongsLists.clear()

        offlineSongsDB.getLists(p).onStart {
            isLoading = true
            doShowMoreLoading = false
        }.catch {
            isLoading = false
            doShowMoreLoading = false
        }.collectLatest {
            if (p == 0) offlineSongsLists.clear()
            isLoading = false
            doShowMoreLoading = it.size >= 30
            offlineSongsLists.addAll(it)
        }
    }
}