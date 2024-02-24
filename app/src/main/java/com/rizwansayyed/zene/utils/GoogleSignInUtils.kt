package com.rizwansayyed.zene.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.domain.LoginUserData
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GoogleSignInUtils(
    val c: Context, val googleListener: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    private val serverId =
        "248438081408-7c3iha16b9b1ea99hm7lt9mvp826c4c3.apps.googleusercontent.com"

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(serverId)
        .requestEmail()
        .build()

    val mGoogleSignInClient = GoogleSignIn.getClient(c, gso)

    private val credentialManager = CredentialManager.create(c)

    private val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverId)
        .build()

    val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption)
        .build()


    @Throws(Exception::class)
    private suspend fun startStartSignIn(result: GetCredentialResponse) {
        var isCustomCredential = false

        when (result.credential) {
            is CustomCredential -> isCustomCredential = true
        }

        if (!isCustomCredential) return
        if (result.credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) return


        val g = GoogleIdTokenCredential.createFrom(result.credential.data)
        val email = g.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID")
            ?: return

        val l = LoginUserData(g.displayName, email, g.profilePictureUri.toString())
        loginUser = flowOf(l)

        CoroutineScope(Dispatchers.IO).launch {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }


    fun startGoogleStartSignIn(r: ActivityResult) = CoroutineScope(Dispatchers.IO).launch {
        if (r.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(r.data)
            val l = LoginUserData(task.result.displayName, task.result.email, task.result.photoUrl.toString())
            loginUser = flowOf(l)
        } else {
            c.resources.getString(R.string.error_while_login_google).toast()
        }
    }

    suspend fun startLogin() = withContext(Dispatchers.IO) {
        try {
            val result = credentialManager.getCredential(c, request)
            startStartSignIn(result)
        } catch (e: Exception) {
            if (e.message?.lowercase()?.contains("support cred") == true)
                startGoogleLogin()
            else
                e.message?.toast()
        }
    }


    private suspend fun startGoogleLogin() = withContext(Dispatchers.IO) {
        try {
            googleListener.launch(mGoogleSignInClient.signInIntent)
        } catch (e: Exception) {
            e.message?.toast()
        }
    }


}