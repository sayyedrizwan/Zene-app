package com.rizwansayyed.zene.utils

import android.app.Activity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.MainUtils.toast

class BioAuthMetric(val title: Int, private val context: Activity?) {

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(context?.resources?.getString(title) ?: "")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()


    fun checkAuth(success: (Boolean) -> Unit) {
        val executor = ContextCompat.getMainExecutor(context?.applicationContext!!)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                context.resources.getString(R.string.error_while_authentication).toast()
                success(false)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                success(true)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                context.resources.getString(R.string.error_while_authentication).toast()
                success(false)
            }
        }

        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, callback)
        biometricPrompt.authenticate(promptInfo)
    }

}
