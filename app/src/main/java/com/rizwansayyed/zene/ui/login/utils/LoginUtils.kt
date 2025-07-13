package com.rizwansayyed.zene.ui.login.utils

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.datastore.DataStorageManager.signInWithEmailAddress
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.FirebaseEvents.FirebaseEventsParams
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.SnackBarManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class LoginUtils @Inject constructor(private val zeneAPI: ZeneAPIInterface) {

    enum class LoginType(val type: String) {
        GOOGLE("GOOGLE"), FACEBOOK("FACEBOOK"), APPLE("APPLE_ANDROID")
    }

    var isLoading by mutableStateOf(false)
    var loginViaEmail by mutableStateOf<ResponseResult<Boolean>>(ResponseResult.Empty)

    val actionCodeSettings = actionCodeSettings {
        url = "https://www.zenemusic.co/email-login"
        handleCodeInApp = true
        iosBundleId = "com.rizwansayyed.zenemusic"
        setAndroidPackageName(context.packageName, true, "8")
        linkDomain = "https://www.zenemusic.co/"
    }


    private val serverClientId = BuildConfig.GOOGLE_SERVER_KEY
    private val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverClientId).setAutoSelectEnabled(false).build()

    private val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
    private val callbackManager = CallbackManager.Factory.create()


    private val loginManager = LoginManager.getInstance()
    private val facebookLoginList = listOf("email", "public_profile")

    private val appleProvider = OAuthProvider.newBuilder("apple.com").apply {
        scopes = arrayOf("email", "name").toMutableList()
        addCustomParameter("locale", "en")
    }

    fun resetEmailLogin() {
        loginViaEmail = ResponseResult.Empty
    }

    fun startGoogleLogin(activity: Activity) {
        val credentialManager = CredentialManager.create(activity)
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                if (result.credential !is CustomCredential) {
                    isLoading = false
                    return@launch
                }

                val credential = result.credential
                if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    isLoading = false
                    return@launch
                }

                try {
                    val info = GoogleIdTokenCredential.createFrom(credential.data)
                    serverLogin(info.idToken, LoginType.GOOGLE)
                    registerEvents(FirebaseEventsParams.GOOGLE_LOGIN)
                } catch (e: Exception) {
                    e.message?.toast()
                    isLoading = false
                }
            } catch (e: Exception) {
                isLoading = false
            }
        }
    }

    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onCancel() {
            isLoading = false
        }

        override fun onError(error: FacebookException) {
            isLoading = false
        }

        override fun onSuccess(result: LoginResult) {
            CoroutineScope(Dispatchers.IO).launch {
                serverLogin(result.accessToken.token, LoginType.FACEBOOK)
                registerEvents(FirebaseEventsParams.FACEBOOK_LOGIN)
                if (isActive) cancel()
            }
        }
    }

    fun startFacebookLogin(activity: Activity) {
        isLoading = true
        loginManager.logIn(
            activity as ActivityResultRegistryOwner, callbackManager, facebookLoginList
        )

        loginManager.registerCallback(callbackManager, facebookCallback)
    }

    fun startEmailLogin(email: String) = CoroutineScope(Dispatchers.IO).launch {
        loginViaEmail = ResponseResult.Loading
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener {
                loginViaEmail = ResponseResult.Success(it.isSuccessful)
            }
            .addOnFailureListener {
                it.message?.toast()
                loginViaEmail = ResponseResult.Success(false)
            }
    }

    fun startAuthEmailLogin(link: Uri) {
        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            val email = signInWithEmailAddress.firstOrNull() ?: ""
            try {
                val response =
                    Firebase.auth.signInWithEmailLink(email, link.toString()).await()
                val tokenResult = response.user?.getIdToken(true)?.await()

                serverLogin(tokenResult?.token ?: "", LoginType.GOOGLE)
                registerEvents(FirebaseEventsParams.EMAIL_LOGIN)
            } catch (e: Exception) {
                e.message?.toast()
            }

            isLoading = false

            if (isActive) cancel()
        }
    }

    fun startAppleLogin(activity: Activity) = CoroutineScope(Dispatchers.IO).launch {
        isLoading = true

        try {
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            val result =
                auth.startActivityForSignInWithProvider(activity, appleProvider.build()).await()
                    ?: return@launch

            val appleCredential = result.credential as? OAuthCredential
            val idToken = appleCredential?.idToken ?: ""

            serverLogin(idToken, LoginType.APPLE)
            registerEvents(FirebaseEventsParams.APPLE_LOGIN)
        } catch (e: Exception) {
            e.message?.toast()
            isLoading = false
        }
    }

    private suspend fun serverLogin(id: String, type: LoginType) {
        if (id.length < 5) {
            isLoading = false
            return
        }

        zeneAPI.loginUser(id, type.type).catch { isLoading = false }.collectLatest {
            isLoading = false
            if (it.isError == true) SnackBarManager.showMessage(context.resources.getString(R.string.error_login))
            else userInfo = flowOf(it)
        }
    }

}