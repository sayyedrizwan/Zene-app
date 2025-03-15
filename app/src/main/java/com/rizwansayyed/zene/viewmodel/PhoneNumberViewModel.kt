package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.utils.ContactData
import com.rizwansayyed.zene.utils.GetAllContactsUtils
import com.rizwansayyed.zene.utils.MainUtils.countryCodeMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PhoneNumberViewModel @Inject constructor(
    private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var countryCode by mutableStateOf("1")
    var countryCodeLists = mutableListOf<String>()
    var phoneNumber by mutableStateOf("")
    var isPhoneNumberWasVerifiedLatest by mutableStateOf<ResponseResult<Boolean>>(ResponseResult.Empty)

    var contactsUsers = mutableStateListOf<ContactData>()
    var usersOfZene = mutableStateListOf<ConnectUserResponse>()
    var isUsersLoading by mutableStateOf(false)

    var phoneNumberVerify by mutableStateOf<ResponseResult<StatusTypeResponse>>(ResponseResult.Empty)
    var optVerify by mutableStateOf<ResponseResult<StatusTypeResponse>>(ResponseResult.Empty)

    fun getUserCountryCode() = viewModelScope.launch(Dispatchers.IO) {
        countryCodeLists.addAll(countryCodeMap.map { it.value }.sortedDescending())

        val cc = ipDB.firstOrNull()?.countryCode?.uppercase()
        countryCode = if (countryCodeMap.containsKey(cc)) countryCodeMap[cc]!!
        else "1"
    }

    fun setUserCountryCode(v: String) = viewModelScope.launch(Dispatchers.IO) {
        countryCode = v
    }

    fun setPhoneNumber(v: String) = viewModelScope.launch(Dispatchers.IO) {
        phoneNumber = v
    }

    fun resetVerify() = viewModelScope.launch(Dispatchers.IO) {
        phoneNumberVerify = ResponseResult.Empty
    }


    fun sendNumberVerification(phoneNumber: String) = viewModelScope.launch(Dispatchers.IO) {
        val fullPhoneNumber = "$countryCode$phoneNumber"
        zeneAPI.sendVerifyPhoneNumber(fullPhoneNumber).onStart {
            phoneNumberVerify = ResponseResult.Loading
        }.catch {
            phoneNumberVerify = ResponseResult.Error(it)
        }.collectLatest {
            phoneNumberVerify = ResponseResult.Success(it)
        }
    }

    fun verifyOTPNumber(code: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.verifyPhoneNumber(code).onStart {
            optVerify = ResponseResult.Loading
        }.catch {
            optVerify = ResponseResult.Error(it)
        }.collectLatest {
            if (it.status == true) {
                zeneAPI.updateUser().catch {}.collectLatest { u ->
                    DataStorageManager.userInfo = flowOf(u)
                }
            }

            delay(1.seconds)
            optVerify = ResponseResult.Success(it)
        }
    }

    fun checkLastOTPVerifiedInWeek() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.checkNumberVerified().onStart {
            isPhoneNumberWasVerifiedLatest = ResponseResult.Loading
        }.catch {
            isPhoneNumberWasVerifiedLatest = ResponseResult.Error(it)
        }.collectLatest {
            isPhoneNumberWasVerifiedLatest = ResponseResult.Success(it.status ?: false)
        }
    }

    fun syncAllContacts() = viewModelScope.launch(Dispatchers.IO) {
        isUsersLoading = true
        var number = 0
        val contacts = GetAllContactsUtils().getContactList().distinctBy { it.number }

        suspend fun run(c: List<ContactData>) {
            zeneAPI.connectUsersSearch(c).onStart { }.catch {}.collectLatest {
                usersOfZene.addAll(it)
                number += c.size
                if (contacts.size == number) isUsersLoading = false
            }
        }

        usersOfZene.clear()
        contactsUsers.clear()
        contactsUsers.addAll(contacts.sortedBy { it.name })
        contacts.chunked(70).forEach { c ->
            viewModelScope.launch(Dispatchers.IO) {
                run(c)
            }
        }
    }
}