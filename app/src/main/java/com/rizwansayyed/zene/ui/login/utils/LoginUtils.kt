package com.rizwansayyed.zene.ui.login.utils

import android.app.Activity
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
import com.google.firebase.auth.OAuthProvider
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.utils.URLSUtils.FB_GRAPH_ID
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class LoginUtils @Inject constructor(private val zeneAPI: ZeneAPIInterface) {

    var isLoading by mutableStateOf(false)

    private val serverClientId =
        "12742833162-pko059vg6kqvb5pnj3uf8l7sbbih9i2f.apps.googleusercontent.com"

    private val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(true)
        .setServerClientId(serverClientId).setAutoSelectEnabled(true).build()
    private val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
    private val callbackManager = CallbackManager.Factory.create()


    private val loginManager = LoginManager.getInstance()
    private val facebookLoginList = listOf("email", "public_profile")

    private val appleProvider = OAuthProvider.newBuilder("apple.com").apply {
        scopes = arrayOf("email", "name").toMutableList()
        addCustomParameter("locale", "en")
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
                    val email =
                        info.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID")

                    serverLogin(
                        email ?: "", info.displayName ?: "", info.profilePictureUri.toString()
                    )
                } catch (e: Exception) {
                    Log.d("TAG", "startGoogleLogin: dd ${e.message} ")
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.d("TAG", "startGoogleLogin: dd ${e.message} ")
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
            getFacebookInfo(result.accessToken.token)
        }
    }

    fun getFacebookInfo(token: String) = CoroutineScope(Dispatchers.IO).launch {
        val urlBuilder = FB_GRAPH_ID.toHttpUrlOrNull()?.newBuilder()?.apply {
            addQueryParameter("fields", "email,name")
            addQueryParameter("access_token", token)
        }

        try {
            val client = OkHttpClient()
            val request = Request.Builder().url(urlBuilder?.build().toString()).build()
            val call = client.newCall(request).execute()
            val json = JSONObject(call.body?.string() ?: "")
            val name = json.optString("name")
            val email = json.optString("email")
            val photo = " https://graph.facebook.com/${json.optString("id")}/picture?type=large"

            serverLogin(email, name, photo)
        } catch (e: Exception) {
            isLoading = false
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
        var photo = ""

        val result =
            auth.startActivityForSignInWithProvider(activity, appleProvider.build()).await()
                ?: return@launch

        result.user?.providerData?.forEach {
            email = it.email ?: ""
            name = it.displayName ?: ""
            photo = it.photoUrl.toString()
        }

        serverLogin(email, name, photo)
    }

    private suspend fun serverLogin(email: String, name: String, photo: String) {
        if (email.length < 5 || !email.contains("@")) {
            isLoading = false
            return
        }
        zeneAPI.updateUser(email, name, photo).catch {
            isLoading = false
        }.collectLatest {
            userInfo = flowOf(it)
        }
    }

}