package com.rizwansayyed.zene.ui.login.utils

import android.app.Activity
import android.content.Intent
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication
import com.rizwansayyed.zene.ui.login.LoginManagerViewModel
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.SnackBarManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class GoogleLoginHandler @Inject constructor(private val zeneAPI: ZeneAPIInterface) {
    var loginManager: LoginManagerViewModel? = null

    fun setInit(v: LoginManagerViewModel) {
        loginManager = v
    }

    fun startLogin(activity: Activity, onLegacySignInNeeded: (() -> Unit)) {
        startCredentialManagerLogin(activity, onLegacySignInNeeded)
    }

    private fun startCredentialManagerLogin(
        activity: Activity, onLegacySignInNeeded: (() -> Unit)
    ) {
        val credentialManager = CredentialManager.create(activity)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result =
                    credentialManager.getCredential(activity, LoginConfig.googleSignInRequest)
                handleCredentialResult(result)
            } catch (_: GetCredentialException) {
                onLegacySignInNeeded()
            } catch (e: Exception) {
                val error = ZeneBaseApplication.context.resources.getString(R.string.error)
                SnackBarManager.showMessage("$error: ${e.localizedMessage}")
                loginManager?.setLoading(false)
            }
        }
    }

    private suspend fun handleCredentialResult(result: GetCredentialResponse) {
        withContext(Dispatchers.Main) {
            if (result.credential !is CustomCredential) {
                val error = ZeneBaseApplication.context.resources.getString(R.string.error_login)
                SnackBarManager.showMessage(error)
                return@withContext
            }

            val credential = result.credential
            if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val error = ZeneBaseApplication.context.resources.getString(R.string.error_login)
                SnackBarManager.showMessage(error)
                return@withContext
            }

            try {
                val info = GoogleIdTokenCredential.createFrom(credential.data)
                serverLogin(info.idToken, LoginType.GOOGLE)
                registerEvents(FirebaseEvents.FirebaseEventsParams.GOOGLE_LOGIN)
            } catch (e: Exception) {
                e.message?.toast()
            }
        }
    }

    fun handleLegacySignInResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            CoroutineScope(Dispatchers.IO).launch {
                if (task.result.idToken != null) {
                    serverLogin(task.result.idToken!!, LoginType.GOOGLE)
                    registerEvents(FirebaseEvents.FirebaseEventsParams.GOOGLE_LOGIN)
                }
            }
        } else {
            loginManager?.setLoading(false)
            SnackBarManager.showMessage(
                ZeneBaseApplication.context.resources.getString(
                    R.string.error_login
                )
            )
        }
    }

    fun createSignInIntent(): Intent? {
        return try {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(LoginConfig.SERVER_CLIENT_ID).requestEmail().requestProfile()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(ZeneBaseApplication.context, gso)
            googleSignInClient.signInIntent
        } catch (_: Exception) {
            null
        }
    }


    private suspend fun serverLogin(id: String, type: LoginType) {
        loginManager?.setLoading(true)
        if (id.length < 5) {
            loginManager?.setLoading(false)
            return
        }

        zeneAPI.loginUser(id, type.type).catch { loginManager?.setLoading(false) }.collectLatest {
            if (it.isError == true) SnackBarManager.showMessage(
                ZeneBaseApplication.context.resources.getString(
                    R.string.error_login
                )
            )
            else userInfo = flowOf(it)

            loginManager?.setLoading(false)
        }
    }
}