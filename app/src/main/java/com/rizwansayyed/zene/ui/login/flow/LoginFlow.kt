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
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.NavigationUtils.SYNC_DATA
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand
import com.rizwansayyed.zene.utils.Utils.URLS.GOOGLE_BUNDLE_EMAIL
import com.rizwansayyed.zene.utils.Utils.URLS.GRAPH_FB_API
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


class LoginFlow @Inject constructor(private val zeneAPIInterface: ZeneAPIInterface) {
    private var credentialManager: CredentialManager? = null
    private val providerApple = OAuthProvider.newBuilder("apple.com").apply {
        scopes = arrayOf("email", "name").toMutableList()
    }

    private lateinit var close: () -> Unit

    fun init(type: LoginFlowType, c: Activity, close: () -> Unit) {
        credentialManager = CredentialManager.create(c)
        this.close = close

        when (type) {
            LoginFlowType.GOOGLE -> startGoogleSignIn(c)
            LoginFlowType.APPLE -> startAppleSignIn(c)
            LoginFlowType.FACEBOOK -> startFBSignIn(c)
        }
    }

    private fun startGoogleSignIn(c: Activity) = CoroutineScope(Dispatchers.Main).launch {
        try {
            val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_SERVER_ID).setAutoSelectEnabled(false)
                .setAutoSelectEnabled(false).build()

            val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
            val credential = credentialManager?.getCredential(c, request)?.credential

            suspend fun get() {
                try {
                    val g = GoogleIdTokenCredential.createFrom(credential!!.data)
                    val email = g.data.getString(GOOGLE_BUNDLE_EMAIL)

                    logEvents(FirebaseLogEvents.FirebaseEvents.LOGIN_WITH_GOOGLE)
                    startLogin(email, g.displayName, g.profilePictureUri.toString())
                } catch (e: Exception) {
                    close()
                }
            }

            when (credential) {
                is CustomCredential -> if (credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) get()
                else -> close()
            }
        } catch (e: Exception) {
            close()
        }
    }

    private fun startFBSignIn(c: Activity) = CoroutineScope(Dispatchers.Main).launch {
        val callbackManager = CallbackManager.Factory.create()
        val loginManager = LoginManager.getInstance()

        loginManager.logIn(
            c as ActivityResultRegistryOwner,
            callbackManager, listOf("openid", "email", "public_profile")
        )

        val callback = object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                close()
            }

            override fun onError(error: FacebookException) {
                close()
            }

            override fun onSuccess(result: LoginResult) {
                CoroutineScope(Dispatchers.IO).launch {
                    fbGraph(result.accessToken.token)
                }
            }
        }
        loginManager.registerCallback(callbackManager, callback)
    }

    private fun startAppleSignIn(c: Activity) = CoroutineScope(Dispatchers.Main).launch {
        try {
            val auth = FirebaseAuth.getInstance()
                .startActivityForSignInWithProvider(c, providerApple.build()).await()

            if ((auth.user?.providerData?.size ?: 0) <= 0) {
                close()
                return@launch
            }

            auth.user?.providerData?.forEach { user ->
                if (user.email?.contains("@") == true) {
                    logEvents(FirebaseLogEvents.FirebaseEvents.LOGIN_WITH_APPLE)
                    startLogin(user?.email, user?.displayName, user?.photoUrl.toString())
                }
            }

        } catch (e: Exception) {
            close()
        }
    }

    private suspend fun fbGraph(token: String) = withContext(Dispatchers.IO) {
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

            logEvents(FirebaseLogEvents.FirebaseEvents.LOGIN_WITH_FB)
            startLogin(data?.email(), data?.name ?: "", data?.profilePic())
        } catch (e: Exception) {
            close()
        }
    }

    private suspend fun startLogin(e: String?, n: String?, p: String?) =
        withContext(Dispatchers.IO) {
            if (e == null) {
                close()
                return@withContext
            }
            if (e.length <= 3 && !e.contains("@")) {
                close()
                return@withContext
            }

            val user = zeneAPIInterface.getUser(e).catch { }.firstOrNull()
            if (user?.email != null) {
                pinnedArtistsList = flowOf(user.pinned_artists?.filterNotNull()?.toTypedArray())
                DataStoreManager.userInfoDB = flowOf(user.toUserInfo(e))
                sendNavCommand(SYNC_DATA)
                return@withContext
            }

            val u = UserInfoData(
                n, e, 0, p, false, SubscriptionType.FREE.name, null
            )
            DataStoreManager.userInfoDB = flowOf(u)
            delay(1.seconds)
            zeneAPIInterface.updateUser().catch { }.collect()
            sendNavCommand(SYNC_DATA)
        }
}