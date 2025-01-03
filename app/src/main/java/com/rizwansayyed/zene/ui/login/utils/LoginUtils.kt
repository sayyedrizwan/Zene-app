package com.rizwansayyed.zene.ui.login.utils

import android.app.Activity
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
import com.google.firebase.auth.OAuthProvider
import com.rizwansayyed.zene.utils.MainUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginUtils {

    var isLoading by mutableStateOf(false)

    private val serverClientId =
        "12742833162-pko059vg6kqvb5pnj3uf8l7sbbih9i2f.apps.googleusercontent.com"

    private val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(true)
        .setServerClientId(serverClientId).setAutoSelectEnabled(true).build()
    private val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
    private val callbackManager = CallbackManager.Factory.create()


    private val loginManager = LoginManager.getInstance()
    private val facebookLoginList = listOf("email", "public_profile")

    val appleProvider = OAuthProvider.newBuilder("apple.com").apply {
        scopes = arrayOf("email", "name").toMutableList()
        addCustomParameter("locale", "en")
    }


    fun startGoogleLogin(activity: Activity) {
        val credentialManager = CredentialManager.create(activity)
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                when (val credential = result.credential) {
                    is CustomCredential -> {
                        if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            isLoading = false
                            return@launch
                        }
                        try {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)
                            googleIdTokenCredential.displayName?.toast()
                        } catch (e: Exception) {
                            isLoading = false
                        }
                    }

                    else -> isLoading = false
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
            result.accessToken.token.toast()
        }
    }

    fun startFacebookLogin(activity: Activity) {
        isLoading = true
        loginManager.logIn(
            activity as ActivityResultRegistryOwner, callbackManager, facebookLoginList
        )

        loginManager.registerCallback(callbackManager, facebookCallback)
    }

    fun startAppleLogin(activity: Activity) = CoroutineScope(Dispatchers.IO).launch {
        isLoading = true

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        var email = ""
        var name = ""

        val result =
            auth.startActivityForSignInWithProvider(activity, appleProvider.build()).await()
                ?: return@launch

        result.user?.providerData?.forEach {
            email = it.email ?: ""
            name = it.email ?: ""
        }
    }


    fun serverLogin() {

    }

}