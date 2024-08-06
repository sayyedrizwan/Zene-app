package com.rizwansayyed.zene.ui.login.flow

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.FbLoginResponse
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.db.DataStoreManager
import com.rizwansayyed.zene.data.db.DataStoreManager.pinnedArtistsList
import com.rizwansayyed.zene.data.db.model.SubscriptionType
import com.rizwansayyed.zene.data.db.model.UserInfoData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.NavigationUtils.SYNC_DATA
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand
import com.rizwansayyed.zene.utils.Utils.URLS.GOOGLE_BUNDLE_EMAIL
import com.rizwansayyed.zene.utils.Utils.URLS.GRAPH_FB_API
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Arrays
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


class LoginFlow @Inject constructor(private val zeneAPIInterface: ZeneAPIInterface) {
    private var credentialManager: CredentialManager? = null
    private val providerApple = OAuthProvider.newBuilder("apple.com").apply {
        scopes = arrayOf("email", "name").toMutableList()
    }

    fun init(type: LoginFlowType, c: Activity) {
        credentialManager = CredentialManager.create(c)

        when (type) {
            LoginFlowType.GOOGLE -> startGoogleSignIn(c)
            LoginFlowType.APPLE -> startAppleSignIn(c)
            LoginFlowType.FACEBOOK -> startFBSignIn(c)
        }
    }

    private fun startGoogleSignIn(c: Activity) = CoroutineScope(Dispatchers.Main).launch {
        val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_SERVER_ID).setAutoSelectEnabled(false)
            .setAutoSelectEnabled(false).build()

        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val credential = credentialManager?.getCredential(c, request)?.credential

        suspend fun get() {
            try {
                val g = GoogleIdTokenCredential.createFrom(credential!!.data)
                val email = g.data.getString(GOOGLE_BUNDLE_EMAIL)
                startLogin(email, g.displayName, g.profilePictureUri.toString())
            } catch (e: Exception) {
                context.getString(R.string.error_while_login).toast()
            }
        }

        when (credential) {
            is CustomCredential -> if (credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) get()
            else -> context.getString(R.string.error_while_login).toast()
        }
    }

    private fun startFBSignIn(c: Activity) = CoroutineScope(Dispatchers.Main).launch {
        val callbackManager = CallbackManager.Factory.create()
        val loginManager = LoginManager.getInstance()

        loginManager.logIn(
            c as ActivityResultRegistryOwner,
            callbackManager,
            listOf("openid", "email", "public_profile")
        )

        val callback = object : FacebookCallback<LoginResult> {
            override fun onCancel() {}

            override fun onError(error: FacebookException) {
                context.getString(R.string.error_while_login).toast()
            }

            override fun onSuccess(result: LoginResult) {
                fbGraph(result.accessToken.token)
            }

        }

        loginManager.registerCallback(callbackManager, callback)
    }

    private fun startAppleSignIn(c: Activity) = CoroutineScope(Dispatchers.Main).launch {
        try {
            val auth = FirebaseAuth.getInstance()
                .startActivityForSignInWithProvider(c, providerApple.build()).await()

            if ((auth.user?.providerData?.size ?: 0) <= 0) {
                context.getString(R.string.error_while_login).toast()
                return@launch
            }

            auth.user?.providerData?.forEach { user ->
                if (user.email?.contains("@") == true)
                    startLogin(user?.email, user?.displayName, user?.photoUrl.toString())
            }

        } catch (e: Exception) {
            context.getString(R.string.error_while_login).toast()
        }
    }

    private fun fbGraph(token: String) = runBlocking(Dispatchers.IO) {
        val client = OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.MINUTES)
            .build()

        val httpUrl = HttpUrl.Builder().scheme("https").host(GRAPH_FB_API)
            .addPathSegment("me")
            .addQueryParameter("access_token", token)
            .addQueryParameter("fields", "id,name,email,picture.width(640).height(640)")

        try {
            val request = Request.Builder().url(httpUrl.toString()).build()
            val response = client.newCall(request).execute()

            val data =
                moshi.adapter(FbLoginResponse::class.java).fromJson(response.body?.string() ?: "")

            startLogin(data?.email(), data?.name ?: "", data?.profilePic())
        } catch (e: Exception) {
            context.getString(R.string.error_while_login).toast()
        }
    }

    private suspend fun startLogin(e: String?, n: String?, p: String?) =
        runBlocking(Dispatchers.IO) {
            if (e == null) {
                context.getString(R.string.error_while_login).toast()
                return@runBlocking
            }
            if (e.length <= 3 && !e.contains("@")) {
                context.getString(R.string.error_while_login).toast()
                return@runBlocking
            }

            val user = zeneAPIInterface.getUser(e).firstOrNull()
            if (user?.email != null) {
                pinnedArtistsList = flowOf(user.pinned_artists?.filterNotNull()?.toTypedArray())
                DataStoreManager.userInfoDB = flowOf(user.toUserInfo(e))
                sendNavCommand(SYNC_DATA)
                return@runBlocking
            }

            val u = UserInfoData(
                n, e, 0, p, false, SubscriptionType.FREE.name, null
            )
            DataStoreManager.userInfoDB = flowOf(u)
            delay(1.seconds)
            zeneAPIInterface.updateUser().firstOrNull()
            sendNavCommand(SYNC_DATA)
        }
}