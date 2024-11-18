package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.zene.TrueCallerAPIInterface
import com.rizwansayyed.zene.utils.Utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerificationViewModel @Inject constructor(
    private val trueCallerAPI: TrueCallerAPIInterface
) : ViewModel() {

    var isError by mutableStateOf(false)
    var isOTPSend by mutableStateOf(false)

    fun getNumber(code: String, codeVerifier: String) = viewModelScope.launch(Dispatchers.IO) {
        trueCallerAPI.userInfo(code, codeVerifier).onStart {}.catch {
            isError = true
        }.collectLatest {
            if (it.phone_number_verified)
                updateNumber(it.phone_number)
            else
                isError = true
        }
    }

    fun sendOTP(phoneNumber: String, countryCode: String) = viewModelScope.launch(Dispatchers.IO) {
        isOTPSend = true
    }

    fun updateNumber(phoneNumber: String) = viewModelScope.launch(Dispatchers.IO) {
        phoneNumber.toast()
    }
}