package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.zene.TrueCallerAPIInterface
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
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
class PhoneVerificationViewModel @Inject constructor(
    private val trueCallerAPI: TrueCallerAPIInterface,
    private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var isError by mutableStateOf(false)
    var isOTPSendLoading by mutableStateOf(false)
    var isOTPSend by mutableStateOf(false)
    var otpTimeout by mutableStateOf(false)

    var otpSuccess by mutableStateOf<APIResponse<Boolean>>(APIResponse.Empty)

    fun getNumber(code: String, codeVerifier: String) = viewModelScope.launch(Dispatchers.IO) {
        trueCallerAPI.userInfo(code, codeVerifier).onStart {}.catch {
            isError = true
        }.collectLatest {
            if (it.phone_number_verified) updateNumber(it.phone_number, "91", "1445")
            else isError = true
        }
    }

    fun sendOTP(phoneNumber: String, countryCode: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.numberVerification(phoneNumber, countryCode).onStart {
            isOTPSendLoading = true
        }.catch {
            isOTPSendLoading = false
        }.collectLatest {
            if (it.status == "timeout") {
                otpTimeout = true
            } else {
                isOTPSendLoading = false
                isOTPSend = true
            }
        }
    }

    fun updateNumber(phoneNumber: String, countryCode: String, otp: String) =
        viewModelScope.launch(Dispatchers.IO) {
            zeneAPI.numberVerificationUpdate(phoneNumber, countryCode, otp).onStart {
                otpSuccess = APIResponse.Loading
            }.catch {
                otpSuccess = APIResponse.Error(it)
            }.collectLatest {
                otpSuccess = APIResponse.Success(it.isSuccess())
                if (it.isSuccess()) {
                    val userInfo = userInfoDB.firstOrNull()
                    userInfo?.phonenumber = phoneNumber
                    userInfoDB = flowOf(userInfo)
                } else {
                    delay(1.seconds)
                    otpSuccess = APIResponse.Empty
                }
            }
        }
}