package com.rizwansayyed.zene.viewmodel

import android.provider.ContactsContract
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.CountryCodeModel
import com.rizwansayyed.zene.data.api.zene.TrueCallerAPIInterface
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.data.db.model.ContactListData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.implementation.ZeneConnectRoomDBInterface
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.getAllPhoneCode
import com.rizwansayyed.zene.utils.Utils.getCurrentPhoneCountryCode
import com.rizwansayyed.zene.utils.Utils.hasCountryCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PhoneVerificationViewModel @Inject constructor(
    private val trueCallerAPI: TrueCallerAPIInterface, private val zeneAPI: ZeneAPIInterface,
    private val zeneConnectDB: ZeneConnectRoomDBInterface,
) : ViewModel() {

    var isError by mutableStateOf(false)
    var isOTPSendLoading by mutableStateOf(false)
    var isOTPSend by mutableStateOf(false)
    var otpTimeout by mutableStateOf(false)
    var contactsLists = mutableStateListOf<ContactListData>()

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


    fun getContactsLists() = CoroutineScope(Dispatchers.IO).launch {
        val list = ArrayList<ContactListData>()
        val countryCodes = getAllPhoneCode()
        val phoneNumberCode = getCurrentPhoneCountryCode()

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        .replace(" ", "").replace("-", "")
                val hasCountryCode = hasCountryCode(number, countryCodes)
                val finalNumber = if (hasCountryCode) number else "+${phoneNumberCode}${number}"
                if (!list.any { n -> n.number == finalNumber })
                    list.add(ContactListData(finalNumber, name))
            }
        }

        contactsLists.clear()
        contactsLists.addAll(list)

        list.chunked(100).parallelStream().forEach { numbers ->
            CoroutineScope(Dispatchers.IO).launch {
                val l = numbers.map { it.number }.filter { it.length > 4 }.toTypedArray()
                zeneAPI.numberUserInfo(l).catch {}.collectLatest {
                    zeneConnectDB.insert(it, list, phoneNumberCode).catch {}.collectLatest { }
                }
                if (isActive) cancel()
            }
        }
    }
}