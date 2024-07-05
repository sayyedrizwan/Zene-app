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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizwansayyed.zene.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginFlow(private val c: Activity, type: LoginFlowType) {
    private val credentialManager = CredentialManager.create(c)

    init {
        when (type) {
            LoginFlowType.GOOGLE -> startGoogleSignIn()
            LoginFlowType.FACEBOOK -> startFBSignIn()
            LoginFlowType.MICROSOFT -> {}
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
                Log.d("TAG", "google sign in Received an invalid google id token response", e)
            }
        }

        when (credential) {
            is CustomCredential -> if (credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) get()
        }
    }

    private fun startFBSignIn() = CoroutineScope(Dispatchers.Main).launch {
        val callbackManager = CallbackManager.Factory.create()
    }

    private fun startLogin(e: String?, n: String?, p: String?) {

    }

}