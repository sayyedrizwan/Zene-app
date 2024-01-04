package com.rizwansayyed.zene.utils

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.domain.LoginUserData
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

class GoogleSignInUtils(val c: Context) {
    private val serverId =
        "248438081408-7c3iha16b9b1ea99hm7lt9mvp826c4c3.apps.googleusercontent.com"

    private val credentialManager = CredentialManager.create(c)

    private val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
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

    suspend fun startLogin() = withContext(Dispatchers.IO) {
        try {
            val result = credentialManager.getCredential(c, request)
            startStartSignIn(result)
        } catch (e: Exception) {
            e.message?.toast()
        }
    }
}