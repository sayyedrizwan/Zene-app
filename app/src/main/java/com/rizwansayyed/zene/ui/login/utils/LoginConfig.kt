package com.rizwansayyed.zene.ui.login.utils

import android.os.Build
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.actionCodeSettings
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.di.ZeneBaseApplication
import java.util.UUID

enum class LoginType(val type: String) {
    GOOGLE("GOOGLE"),
    FACEBOOK("FACEBOOK"),
    APPLE("APPLE_ANDROID"),
    EMAIL("EMAIL")
}

object LoginConfig {
    const val MIN_PLAY_SERVICES_VERSION = 12451000
    const val SERVER_CLIENT_ID = BuildConfig.GOOGLE_SERVER_KEY

    val actionCodeSettings = actionCodeSettings {
        url = "https://www.zenemusic.co/email-login"
        handleCodeInApp = true
        iosBundleId = "com.rizwansayyed.zenemusic"
        setAndroidPackageName(ZeneBaseApplication.context.packageName, true, "8")
        linkDomain = "https://www.zenemusic.co/"
    }

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(SERVER_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .setNonce(generateNonce())
        .build()

    val googleSignInRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    private fun generateNonce(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    fun isCredentialManagerAvailable(): Boolean {
        return try {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ||
                    isGooglePlayServicesVersionSupported()
        } catch (_: Exception) {
            false
        }
    }

    fun isGooglePlayServicesVersionSupported(): Boolean {
        return try {
            val packageManager = ZeneBaseApplication.context.packageManager
            val packageInfo = packageManager.getPackageInfo("com.google.android.gms", 0)
            packageInfo.versionCode >= MIN_PLAY_SERVICES_VERSION
        } catch (_: Exception) {
            false
        }
    }

    fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode =
            googleApiAvailability.isGooglePlayServicesAvailable(ZeneBaseApplication.context)
        return resultCode == ConnectionResult.SUCCESS
    }
}
