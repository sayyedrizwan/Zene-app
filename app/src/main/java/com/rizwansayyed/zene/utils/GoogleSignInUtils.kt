package com.rizwansayyed.zene.utils

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.domain.LoginUserData
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

    private val auth = FirebaseAuth.getInstance()

    private suspend fun login(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val task = auth.signInWithCredential(firebaseCredential).await()
        if (task.user != null) {
            val l = LoginUserData(
                task.user!!.displayName, task.user!!.email, task.user!!.photoUrl.toString()
            )
            loginUser = flowOf(l)

            auth.signOut()
        } else {
            "error".toast()
        }
    }

    private suspend fun startStartSignIn(result: GetCredentialResponse) {
        when (val r = result.credential) {
            is CustomCredential -> {
                if (r.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val g = GoogleIdTokenCredential.createFrom(r.data)
                        login(g.idToken)
                    } catch (e: Exception) {
                        "error".toast()
                    }
                }
            }
            else -> "error".toast()
        }

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