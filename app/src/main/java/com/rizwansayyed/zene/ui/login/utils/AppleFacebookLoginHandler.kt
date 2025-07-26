package com.rizwansayyed.zene.ui.login.utils

import android.app.Activity
import android.net.Uri
import androidx.activity.result.ActivityResultRegistryOwner
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication
import com.rizwansayyed.zene.ui.login.LoginManagerViewModel
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.SnackBarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppleFacebookLoginHandler @Inject constructor(private val zeneAPI: ZeneAPIInterface) {
    private val callbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManagerInstance by lazy { LoginManager.getInstance() }
    private val permissions = listOf("email", "public_profile")

    private val appleProvider = OAuthProvider.newBuilder("apple.com").apply {
        scopes = arrayOf("email", "name").toMutableList()
        addCustomParameter("locale", "en")
    }

    var loginManager: LoginManagerViewModel? = null

    fun setInit(v: LoginManagerViewModel) {
        loginManager = v
    }

    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onCancel() {
            loginManager?.setLoading(false)
        }

        override fun onError(error: FacebookException) {
            error.message?.toast()
            loginManager?.setLoading(false)
        }

        override fun onSuccess(result: LoginResult) {
            CoroutineScope(Dispatchers.IO).launch {
                serverLogin(result.accessToken.token, LoginType.FACEBOOK)
                registerEvents(FirebaseEvents.FirebaseEventsParams.FACEBOOK_LOGIN)
            }
        }
    }

    fun startFBLogin(activity: Activity) {
        loginManagerInstance.logIn(
            activity as ActivityResultRegistryOwner, callbackManager, permissions
        )
        loginManagerInstance.registerCallback(callbackManager, facebookCallback)
    }


    fun startAppleLogin(activity: Activity) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val auth = FirebaseAuth.getInstance()
            val result =
                auth.startActivityForSignInWithProvider(activity, appleProvider.build()).await()
                    ?: return@launch

            val appleCredential = result.credential as? OAuthCredential
            val idToken = appleCredential?.idToken ?: ""

            serverLogin(idToken, LoginType.APPLE)
            registerEvents(FirebaseEvents.FirebaseEventsParams.APPLE_LOGIN)
        } catch (e: Exception) {
            e.message?.toast()
            loginManager?.setLoading(false)
        }
    }

    fun sendSignInLink(email: String): Flow<ResponseResult<Boolean>> = flow {
        emit(ResponseResult.Loading)
        try {
            Firebase.auth.sendSignInLinkToEmail(email, LoginConfig.actionCodeSettings).await()
            emit(ResponseResult.Success(true))
        } catch (e: Exception) {
            e.message?.toast()
            emit(ResponseResult.Success(false))
        }
    }

    fun signInWithEmailLink(email: String, link: Uri) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = Firebase.auth.signInWithEmailLink(email, link.toString()).await()

            val tokenResult = response.user?.getIdToken(true)?.await()

            serverLogin(tokenResult?.token ?: "", LoginType.EMAIL)
            registerEvents(FirebaseEvents.FirebaseEventsParams.EMAIL_LOGIN)
        } catch (e: Exception) {
            e.message?.toast()
            loginManager?.setLoading(false)
        }
    }

    private suspend fun serverLogin(id: String, type: LoginType) {
        if (id.length < 5) {
            loginManager?.setLoading(false)
            return
        }

        zeneAPI.loginUser(id, type.type).catch { loginManager?.setLoading(false) }.collectLatest {
            loginManager?.setLoading(false)
            if (it.isError == true) SnackBarManager.showMessage(
                ZeneBaseApplication.Companion.context.resources.getString(
                    R.string.error_login
                )
            )
            else userInfo = flowOf(it)
        }
    }
}