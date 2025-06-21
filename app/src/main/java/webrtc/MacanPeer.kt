package com.macan.virtualcam.webrtc

import android.content.Context
import org.webrtc.*

class MacanPeer(
    context: Context,
    eglContext: EglBase.Context
) {
    val peerConnection: PeerConnection
    val videoTrack: VideoTrack
    val videoSource: VideoSource
    private val factory: PeerConnectionFactory

    init {
        // Init WebRTC global
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions()
        )

        // Create factory
        factory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglContext))
            .createPeerConnectionFactory()

        // Create videoSource and track
        videoSource = factory.createVideoSource(false)
        videoTrack = factory.createVideoTrack("macan_track", videoSource)

        // Setup peer
        peerConnection = factory.createPeerConnection(
            PeerConnection.RTCConfiguration(emptyList()),
            object : PeerConnection.Observer {
                override fun onIceCandidate(p0: IceCandidate?) {}
                override fun onAddStream(p0: MediaStream?) {}
                override fun onTrack(transceiver: RtpTransceiver?) {}
                override fun onConnectionChange(state: PeerConnection.PeerConnectionState?) {}
                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {}
                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {}
                override fun onSignalingChange(state: PeerConnection.SignalingState?) {}
                override fun onDataChannel(p0: DataChannel?) {}
                override fun onRemoveStream(p0: MediaStream?) {}
                override fun onRenegotiationNeeded() {}
                override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
                override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {}
                override fun onSelectedCandidatePairChanged(p0: CandidatePairChangeEvent?) {}
            }
        )!!

        peerConnection.addTrack(videoTrack)
    }

    fun createOffer(onSdpReady: (String) -> Unit) {
        val constraints = MediaConstraints()
        peerConnection.createOffer(object : SdpObserver {
            override fun onCreateSuccess(desc: SessionDescription) {
                peerConnection.setLocalDescription(this, desc)
                onSdpReady(desc.description)
            }
            override fun onCreateFailure(reason: String?) {}
            override fun onSetSuccess() {}
            override fun onSetFailure(reason: String?) {}
        }, constraints)
    }

    fun setRemoteAnswer(answerSdp: String) {
        val answer = SessionDescription(SessionDescription.Type.ANSWER, answerSdp)
        peerConnection.setRemoteDescription(object : SdpObserver {
            override fun onSetSuccess() {}
            override fun onSetFailure(reason: String?) {}
            override fun onCreateSuccess(p0: SessionDescription?) {}
            override fun onCreateFailure(p0: String?) {}
        }, answer)
    }
}
