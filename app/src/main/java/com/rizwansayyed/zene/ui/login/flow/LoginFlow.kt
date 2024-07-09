package com.rizwansayyed.zene.ui.login.flow

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.db.DataStoreManager
import com.rizwansayyed.zene.data.db.model.SubscriptionType
import com.rizwansayyed.zene.data.db.model.UserInfoData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


class LoginFlow @Inject constructor(private val zeneAPIInterface: ZeneAPIInterface) {
    private var credentialManager: CredentialManager? = null
    private val providerApple = OAuthProvider.newBuilder("apple.com").apply {
        scopes = mutableListOf("email", "name")
    }

    private val providerMicrosoft = OAuthProvider.newBuilder("microsoft.com")

    fun init(type: LoginFlowType, c: Activity) {
        credentialManager = CredentialManager.create(c)

        when (type) {
            LoginFlowType.GOOGLE -> startGoogleSignIn(c)
            LoginFlowType.APPLE -> startAppleSignIn(c)
            LoginFlowType.MICROSOFT -> startAppleSignIn(c)
        }
    }

    private fun startGoogleSignIn(c: Activity) = CoroutineScope(Dispatchers.Main).launch {
        startLogin("sayyedrizwanahmed@gmail.com", "Rizwan Sayyed", "")
        return@launch

        val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.GOOGLE_SERVER_ID).setAutoSelectEnabled(false)
            .setAutoSelectEnabled(true).build()

        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val credential = credentialManager?.getCredential(c, request)?.credential

        suspend fun get() {
            try {
                val g = GoogleIdTokenCredential.createFrom(credential!!.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(g.idToken, null)
                val c = Firebase.auth.signInWithCredential(firebaseCredential).await()
                startLogin(c.user?.email, c.user?.displayName, c.user?.photoUrl.toString())
            } catch (e: Exception) {
                context.getString(R.string.error_while_login).toast()
            }
        }

        when (credential) {
            is CustomCredential -> if (credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) get()
            else -> context.getString(R.string.error_while_login).toast()
        }
    }

    private fun startAppleSignIn(c: Activity) = CoroutineScope(Dispatchers.Main).launch {
        val pending = Firebase.auth.pendingAuthResult
        if (pending == null) {
            val result = Firebase.auth
                .startActivityForSignInWithProvider(c, providerApple.build()).await()
            val user = result.user
            startLogin(user?.email, user?.displayName, user?.photoUrl.toString())
        } else {
            val result = pending.await().user
            startLogin(result?.email, result?.displayName, result?.photoUrl.toString())
        }
    }

    private suspend fun startLogin(e: String?, n: String?, p: String?) {
        val user = zeneAPIInterface.getUser(e ?: "").firstOrNull()
        if (user?.email != null) {
            val u = UserInfoData(
                user.name, e, user.total_playtime, user.profile_photo,
                user.isReviewDone(), user.subscription_status, user.subscription_status
            )
            DataStoreManager.userInfoDB = flowOf(u)
            return
        }

        val u = UserInfoData(
            n, e, 0, p, false, SubscriptionType.FREE.name, null
        )
        DataStoreManager.userInfoDB = flowOf(u)
        delay(2.seconds)
        zeneAPIInterface.updateUser().firstOrNull()
        e?.toast()

    }

}