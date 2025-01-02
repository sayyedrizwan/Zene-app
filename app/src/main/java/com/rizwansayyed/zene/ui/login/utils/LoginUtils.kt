package com.rizwansayyed.zene.ui.login.utils

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LoginUtils {

    fun startGoogleLogin(activity: Activity) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId("12742833162-pko059vg6kqvb5pnj3uf8l7sbbih9i2f.apps.googleusercontent.com")
            .setAutoSelectEnabled(true).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        val credentialManager = CredentialManager.create(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential( activity, request)
                val credential = result.credential

                when (credential) {
                    is CustomCredential -> {
                        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            try {
                                val googleIdTokenCredential = GoogleIdTokenCredential
                                    .createFrom(credential.data)
                                googleIdTokenCredential.displayName?.toast()
                            } catch (e: Exception) {
                                Log.e("TAG", "Received an invalid google id token response", e)
                            }
                        } else {
                            // Catch any unrecognized custom credential type here.
                            Log.e("TAG", "Unexpected type of credential")
                        }
                    }

                    else -> {
                        // Catch any unrecognized credential type here.
                        Log.e("TAG", "Unexpected type of credential")
                    }
                }
            } catch (e: Exception) {
//                handleFailure(e)
            }
        }

    }
}