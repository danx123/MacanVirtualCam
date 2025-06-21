package com.macan.virtualcam.render

import android.content.Context
import android.net.Uri
import android.view.Surface
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.Player


class MacanVideoController(
    private val context: Context,
    private val uri: Uri
) {

    private var player: ExoPlayer? = null
    private var surface: Surface? = null

    fun attachSurface(targetSurface: Surface) {
        surface = targetSurface
        player?.setVideoSurface(surface)
    }

    fun prepare() {
        player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = Player.REPEAT_MODE_ALL // 🔁 Looping
            prepare()
        }

        // Pasang Surface kalau sudah ada
        surface?.let {
            player?.setVideoSurface(it)
        }
    }

    fun play() {
        player?.playWhenReady = true
    }

    fun pause() {
        player?.playWhenReady = false
    }

    fun stop() {
        player?.stop()
    }

    fun release() {
        player?.release()
        player = null
        surface?.release()
        surface = null
    }
}
