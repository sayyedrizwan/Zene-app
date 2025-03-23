package com.rizwansayyed.zene.ui.main.connect.utils

import android.util.Log
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceServer
import org.webrtc.PeerConnection.RTCConfiguration
import org.webrtc.PeerConnectionFactory

object ConnectUserLiveConnection {

    private val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(context)
        .setEnableInternalTracer(true).createInitializationOptions()

    private val listener = object : PeerConnection.Observer {
        override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
            Log.d("peerconnection", "onSignalingChange: signal change $p0")
        }

        override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
            Log.d("peerconnection", "onSignalingChange: connection change $p0")
        }

        override fun onIceConnectionReceivingChange(p0: Boolean) {
            Log.d("peerconnection", "onSignalingChange: receiving change $p0")
        }

        override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
            Log.d("peerconnection", "onIceGatheringChange: $p0")
        }

        override fun onIceCandidate(p0: IceCandidate?) {
            Log.d("peerconnection", "onIceGatheringChange: $p0")
        }

        override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
            Log.d("peerconnection", "onIceCandidatesRemoved: $p0")
        }

        override fun onAddStream(p0: MediaStream?) {
            Log.d("peerconnection", "onAddStream: ${p0?.id}")
        }

        override fun onRemoveStream(p0: MediaStream?) {
            Log.d("peerconnection", "onAddStream: ${p0?.id}")
        }

        override fun onDataChannel(p0: DataChannel?) {
            Log.d("peerconnection", "onAddStream: ${p0?.id()}")
        }

        override fun onRenegotiationNeeded() {
            Log.d("peerconnection", "onRenegotiationNeeded:")
        }

    }

    fun startConnection(email: String?) {
        PeerConnectionFactory.initialize(initializationOptions)

        val peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory()

        val iceServers: MutableList<IceServer> = ArrayList()
        iceServers.add(IceServer.builder("stun:stun.l.google.com:19302").createIceServer())

        val rtcConfig = RTCConfiguration(iceServers)

        peerConnectionFactory.createPeerConnection(rtcConfig, listener)
    }
}