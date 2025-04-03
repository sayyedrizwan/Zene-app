package com.rizwansayyed.zene.ui.main

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.utils.WebViewUtils.enable


@Composable
fun WebViewTestOff() {
    var html = """
        
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
              const otherID = "say12233222233";
              const myID = "kkkk12233222233";

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
                  alert("Error accessing camera/microphone. Please check permissions.");
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

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                enable()
                webChromeClient = object : WebChromeClient() {
                    override fun onPermissionRequest(request: PermissionRequest) {
                        request.grant(request.resources)
                    }

                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        super.onConsoleMessage(consoleMessage)

                        Log.d("TAG", "onConsoleMessage: data ${consoleMessage?.message()}")

                        return false
                    }
                }
                loadDataWithBaseURL("https://www.zenemusic.co", html, "text/html", "UTF-8", null)
            }
        }, Modifier
            .fillMaxSize()
            .background(Color.Black)
    )

}