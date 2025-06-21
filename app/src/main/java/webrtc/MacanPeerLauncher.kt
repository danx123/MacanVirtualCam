package com.macan.virtualcam.webrtc

import android.content.Context
import android.view.Surface
import org.webrtc.EglBase
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoFrame
import org.webrtc.VideoSink

class MacanPeerLauncher(
    private val context: Context,
    private val sink: VideoSink // Bisa WebRTC viewer / local renderer
) {
    private val egl = EglBase.create()
    val peer = MacanPeer(context, egl.eglBaseContext)

    val surfaceHelper: SurfaceTextureHelper =
        SurfaceTextureHelper.create("MacanSurfacePush", egl.eglBaseContext)

    val surface: Surface = Surface(surfaceHelper.surfaceTexture)

    init {
        // Pasang listener buat stream video ke videoSource
        surfaceHelper.startListening { frame: VideoFrame ->
            peer.videoSource.capturerObserver.onFrameCaptured(frame)
            sink.onFrame(frame) // Optional: preview lokal
        }
    }

    fun createOffer(onSdp: (String) -> Unit) {
        peer.createOffer(onSdp)
    }

    fun receiveAnswer(answerSdp: String) {
        peer.setRemoteAnswer(answerSdp)
    }

    fun release() {
        surfaceHelper.stopListening()
        surfaceHelper.dispose()
        surface.release()
    }
}
