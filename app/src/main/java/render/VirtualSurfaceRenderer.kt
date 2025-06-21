package com.macan.virtualcam.render

import android.content.Context
import android.graphics.SurfaceTexture
import android.net.Uri
import android.view.Surface
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class VirtualSurfaceRenderer(
    private val context: Context,
    private val uri: Uri,
    private val width: Int = 1280,
    private val height: Int = 720
) {

    private var surfaceTexture: SurfaceTexture? = null
    private var surface: Surface? = null
    private var player: ExoPlayer? = null

    fun initRenderer(): SurfaceTexture {
        surfaceTexture = SurfaceTexture(0).apply {
            setDefaultBufferSize(width, height)
        }
        surface = Surface(surfaceTexture)
        return surfaceTexture!!
    }

    fun startPlayback() {
        surface?.let {
            player = ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(uri))
                setVideoSurface(it)
                prepare()
                playWhenReady = true
            }
        }
    }

    // 🆕 Overload baru: Render langsung ke SurfaceTexture dari WebRTC
    fun renderTo(targetTexture: SurfaceTexture) {
        player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            setVideoSurface(Surface(targetTexture))
            prepare()
            playWhenReady = true
        }
    }

    fun stopPlayback() {
        player?.release()
        surface?.release()
        surfaceTexture?.release()

        player = null
        surface = null
        surfaceTexture = null
    }

    fun getSurfaceTexture(): SurfaceTexture? = surfaceTexture
    fun getSurface(): Surface? = surface
}
