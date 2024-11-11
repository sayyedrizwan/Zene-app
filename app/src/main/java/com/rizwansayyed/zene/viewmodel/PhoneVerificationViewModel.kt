package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneUpdateAvailabilityItem
import com.rizwansayyed.zene.data.api.zene.TrueCallerAPIInterface
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

    var isAppUpdateAvailable by mutableStateOf<APIResponse<ZeneUpdateAvailabilityItem>>(APIResponse.Empty)

    fun getNumber(code: String, codeVerifier: String) = viewModelScope.launch(Dispatchers.IO) {
        trueCallerAPI.userInfo(code, codeVerifier).onStart {
            Log.d("TAG", "onSuccess: runnnedd runnedd")
        }.catch {
            Log.d("TAG", "onSuccess: runnnedd verify ${it.message}")
        }.collectLatest {
            Log.d("TAG", "onSuccess: runnnedd verify ${it}")
        }
    }

}