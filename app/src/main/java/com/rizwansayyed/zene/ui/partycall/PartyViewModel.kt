package com.rizwansayyed.zene.ui.partycall

import android.app.Activity
import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

class PartyViewModel : ViewModel() {
    var isSpeaker by mutableStateOf(false)
    var isPIPMode by mutableStateOf(false)

    var jobCallCancel by mutableStateOf<Job?>(null)

    var profilePhoto by mutableStateOf("")
    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var type by mutableIntStateOf(0)
    var isCallPicked by mutableStateOf(false)
    var noUsers by mutableStateOf(false)
    var callDeclined by mutableStateOf(false)
    var noCallAnswered by mutableStateOf(false)
    var hideCallingView by mutableStateOf(false)
    var isVideoOn by mutableStateOf(false)
    var isMicOn by mutableStateOf(true)
    var randomCallCode by mutableStateOf("")
    var callWebViewMain by mutableStateOf<WebView?>(null)

    fun setInfo(profilePhoto: String, email: String, name: String, type: Int) {
        this.profilePhoto = profilePhoto
        this.email = email
        this.name = name
        this.type = type
    }

    fun setSpeaker() {
        this.isSpeaker = !this.isSpeaker
    }

    fun setPIP(v: Boolean) {
        this.isPIPMode = v
    }

    fun setCode(v: String?) {
        randomCallCode = v ?: generateUniqueIdForCall()
    }

    fun setCallWebView(v: WebView?) {
        callWebViewMain = v
    }

    fun setNoUserOnCall() {
        noUsers = true
    }

    fun setCallPicked() {
        isCallPicked = true
        jobCallCancel?.cancel()
    }

    fun setCallEnded() {
        isCallPicked = false
    }

    fun setCallDeclined() {
        callDeclined = true
        isCallPicked = false
    }

    fun hideCallingView() {
        jobCallCancel?.cancel()
        hideCallingView = true
    }

    fun setVideoOnOrOff(v: Boolean) {
        isVideoOn = v
    }

    fun setMicOnOrOff(v: Boolean) {
        isMicOn = v
    }

    fun startJob() {
        jobCallCancel?.cancel()
        jobCallCancel = viewModelScope.launch {
            delay(25.seconds)
            noCallAnswered = true
            setCallEnded()
        }
    }

    private fun generateUniqueIdForCall(): String {
        val timestamp = System.currentTimeMillis().toString()
        val random = UUID.randomUUID().toString().replace("-", "").substring(0, 8)
        val raw = "$timestamp-$email-$random"

        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(raw.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }.substring(0, 40)
    }
}