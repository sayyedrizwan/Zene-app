package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.utils.MainUtils.countryCodeMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneNumberVerificationViewModel @Inject constructor(
    private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var phoneNumber by mutableStateOf("")
    var countryCode by mutableStateOf("1")
    var countryCodeLists = mutableListOf<String>()

    var phoneNumberVerify by mutableStateOf<ResponseResult<StatusTypeResponse>>(ResponseResult.Empty)

    fun getUserCountryCode() = viewModelScope.launch(Dispatchers.IO) {
        countryCodeLists.addAll(countryCodeMap.map { it.value }.sortedDescending())

        val cc = ipDB.firstOrNull()?.countryCode?.uppercase()
        countryCode = if (countryCodeMap.containsKey(cc)) countryCodeMap[cc]!!
        else "1"
    }

    fun setUserCountryCode(v: String) = viewModelScope.launch(Dispatchers.IO) {
        countryCode = v
    }

    fun setUserPhoneNumber(v: String) = viewModelScope.launch(Dispatchers.IO) {
        phoneNumber = v
    }

    fun sendNumberVerification(phoneNumber: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.sendVerifyPhoneNumber(phoneNumber).onStart {
            phoneNumberVerify = ResponseResult.Loading
        }.catch {
            phoneNumberVerify = ResponseResult.Error(it)
        }.collectLatest {
            phoneNumberVerify = ResponseResult.Success(it)
        }
    }
}