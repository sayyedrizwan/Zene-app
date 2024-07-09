package com.rizwansayyed.zene.ui.login.flow

import android.app.Activity
import android.util.Log
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
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginFlow(private val c: Activity, type: LoginFlowType, private val success: () -> Unit) {
    private val credentialManager = CredentialManager.create(c)
    private val provider = OAuthProvider
        .newBuilder(if (type == LoginFlowType.APPLE) "apple.com" else "microsoft.com").apply {
            if (type == LoginFlowType.APPLE) scopes = mutableListOf("email", "name")
        }

    init {
        when (type) {
            LoginFlowType.GOOGLE -> startGoogleSignIn()
            LoginFlowType.APPLE -> startAppleSignIn()
            LoginFlowType.MICROSOFT -> startAppleSignIn()
        }
    }

    private fun startGoogleSignIn() = CoroutineScope(Dispatchers.Main).launch {
        val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(true)
            .setServerClientId(BuildConfig.GOOGLE_SERVER_ID).setAutoSelectEnabled(false)
            .setAutoSelectEnabled(true).build()

        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val credential = credentialManager.getCredential(c, request).credential

        suspend fun get() {
            try {
                val g = GoogleIdTokenCredential.createFrom(credential.data)
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

    private fun startAppleSignIn() = CoroutineScope(Dispatchers.Main).launch {
        val pending = Firebase.auth.pendingAuthResult
        if (pending == null) {
            val result =
                Firebase.auth.startActivityForSignInWithProvider(c, provider.build()).await()
            val user = result.user
            startLogin(user?.email, user?.displayName, user?.photoUrl.toString())
        } else {
            val result = pending.await().user
            startLogin(result?.email, result?.displayName, result?.photoUrl.toString())
        }
    }

    private fun startLogin(e: String?, n: String?, p: String?) {
        success()
        e?.toast()
    }

}