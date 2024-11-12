package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val updateDB: UpdatesRoomDBInterface
) : ViewModel() {

    var updateLists = mutableStateListOf<UpdateData>()
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
            doShowMoreLoading = it.size >= 20
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
}