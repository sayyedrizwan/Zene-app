package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.utils.MainUtils.countryCodeMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneNumberVerificationViewModel @Inject constructor(
    private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var countryCode by mutableStateOf("1")
    var countryCodeLists = mutableListOf<String>()

    fun getUserCountryCode() = viewModelScope.launch(Dispatchers.IO) {
        countryCodeLists.addAll(countryCodeMap.map { it.value }.sortedDescending())

        val cc = ipDB.firstOrNull()?.countryCode?.uppercase()
        countryCode = if (countryCodeMap.containsKey(cc)) countryCodeMap[cc]!!
        else "1"
    }

    fun setUserCountryCode(v: String) = viewModelScope.launch(Dispatchers.IO) {
        countryCode = v
    }

    fun connectNearMusic() = viewModelScope.launch(Dispatchers.IO) {
//        zeneAPI.connectNearMusic().onStart {
//            nearMusic = ResponseResult.Loading
//        }.catch {
//            nearMusic = ResponseResult.Error(it)
//        }.collectLatest {
//            cacheHelper.save(ZENE_CONNECT_NEAR_MUSIC_API, it)
//            nearMusic = ResponseResult.Success(it)
//        }
    }
}