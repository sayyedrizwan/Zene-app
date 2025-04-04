package com.rizwansayyed.zene.service.party

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL
import com.rizwansayyed.zene.utils.WebViewUtils.enable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PartyService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    private var email: String? = null
    private var myEmail: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        email = intent?.getStringExtra(Intent.EXTRA_EMAIL)

        CoroutineScope(Dispatchers.IO).launch {
            myEmail = DataStorageManager.userInfo.firstOrNull()?.email
        }
        startWebView()

        return START_STICKY
    }


    private fun startWebView() {
        WebView(this).apply {
            enable()
            addJavascriptInterface(CallAppInterface(), "Zene")
            loadDataWithBaseURL(ZENE_URL, htmlString(), "text/html", "UTF-8", null)
        }
    }

    inner class CallAppInterface {
        @JavascriptInterface
        fun videoState() {

        }
    }


    fun htmlString(): String {
        return """
            <html>
          <body>
            <style>
              * {
                margin: 0;
                padding: 0;
                height: 100%;
                background-color: black;
              }

              #localVideo {
                position: fixed;
                width: 100%;
                height: 100%
                object-position: center; 
                object-fit: cover;
              }

              .video-container-remote {
                display: none;
                position: fixed;
                top: 10px;
                right: 10px;
                height: 213px;
              }

              #remoteVideo {
                width: 100%;
                height: 100%;
                object-fit: cover;
                border-radius: 10px;
              }
            </style>

            <video id="remoteVideo"></video>

            <div class="video-container-remote">
              <video id="localVideo" controls></video>
            </div>

            <script src="https://unpkg.com/peerjs@1.5.4/dist/peerjs.min.js"></script>
            <script>
              const myID = "$email";
              const otherID = "$myEmail";
          
              const localVideo = document.getElementById("localVideo");
              const remoteVideo = document.getElementById("remoteVideo");

              let peer = new Peer(myID);

              async function getLocalStream() {
                try {
                 const useBackCamera = true
                 const constraints = {
                    video: {
                        facingMode: useBackCamera ? "environment" : "user",
                        width: { ideal: 99999 },
                        height: { ideal: 99999 }
                    },
                    audio: true
                 };

                  const localStream = await navigator.mediaDevices.getUserMedia(constraints);
                  localStream.width = window.innerWidth * 0.6 * 0.97;
                  localStream.height = window.innerWidth * 0.6 * 0.5625 * 0.97;
            
                  localVideo.srcObject = localStream;
                  localVideo.play();
                  return localStream;
                } catch (error) {
                  console.error("Error accessing media devices:", error);
                }
              }

              async function startCall() {
                const localStream = await getLocalStream();
                if (!localStream) return;

                console.log("Calling:", otherID);
                const call = peer.call(otherID, localStream);

                call.on("stream", (remoteStream) => {
                  remoteVideo.srcObject = remoteStream;
                  remoteVideo.play();
                });

                call.on("error", (err) => console.error("Call error:", err));
                call.on("close", () => console.log("Call ended"));
              }

              peer.on("open", (id) => {
                console.log("My PeerJS ID:", id);
              });

              peer.on("call", async (call) => {
                console.log("Incoming call...");
                const localStream = await getLocalStream();
                if (!localStream) return;

                call.answer(localStream);

                call.on("stream", (remoteStream) => {
                  remoteVideo.srcObject = remoteStream;
                  remoteVideo.play();
                });

                call.on("error", (err) => console.error("Call error:", err));
                call.on("close", () => console.log("Call ended"));
              });


              peer.on("connection", async (call) => {
                call.on("close", () => console.log("Call ended"));
              });

              startCall();
            </script>
          </body>
        </html>
        """.trimIndent()
    }
}