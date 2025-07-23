package com.rizwansayyed.zene.ui.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.login.utils.AppleFacebookLoginHandler
import com.rizwansayyed.zene.ui.login.utils.GoogleLoginHandler
import com.rizwansayyed.zene.utils.MainUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginManagerViewModel @Inject constructor(
    private val googleLoginHandler: GoogleLoginHandler,
    private val loginHandler: AppleFacebookLoginHandler
) : ViewModel() {

    companion object {
        var setEmail by mutableStateOf("")
    }
    var isLoginStateLoading = mutableStateOf(false)

    var loginViaEmail by mutableStateOf<ResponseResult<Boolean>>(ResponseResult.Empty)

    init {
        googleLoginHandler.setInit(this)
        loginHandler.setInit(this)
    }


    fun startGoogleLogin(activity: Activity, onLegacySignInNeeded: (() -> Unit)) {
        setLoading(true)
        googleLoginHandler.startLogin(activity, onLegacySignInNeeded)
    }

    fun startLegacyGoogleLogin(result: ActivityResult) {
        setLoading(true)
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                googleLoginHandler.handleLegacySignInResult(task)
            }
        }
    }

    fun legacyGoogleLogin(): Intent? {
        setLoading(true)
        return googleLoginHandler.createSignInIntent()
    }

    fun startAppleLogin(activity: Activity) {
        setLoading(true)
        loginHandler.startAppleLogin(activity)
    }


    fun startFacebookLogin(activity: Activity) {
        setLoading(true)
        loginHandler.startFBLogin(activity)
    }

    fun startEmailLogin(email: String) {
        loginViaEmail = ResponseResult.Loading
        viewModelScope.launch(Dispatchers.IO) {
            setEmail = email
            loginHandler.sendSignInLink(email).catch {
                loginViaEmail = ResponseResult.Empty
            }.collectLatest {
                loginViaEmail = it
                setLoading(false)
            }
        }
    }

    fun signInWithEmailLink(link: Uri) {
        setLoading(true)
        loginHandler.signInWithEmailLink(setEmail, link)
    }

    fun resetEmailLogin() {
        setEmail = ""
    }

    fun setLoading(loading: Boolean) {
        isLoginStateLoading.value = loading
    }
}