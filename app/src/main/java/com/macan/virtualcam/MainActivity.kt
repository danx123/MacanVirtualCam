package com.macan.virtualcam

import android.view.Surface
import android.net.Uri
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.macan.virtualcam.render.VirtualSurfaceRenderer
import com.macan.virtualcam.render.MacanVideoController

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private var selectedUri: Uri? = null
    private var virtualRenderer: VirtualSurfaceRenderer? = null
    private var videoController: MacanVideoController? = null

    private val launcher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedUri = it

            // ExoPlayer (PlayerView) playback
            val mediaItem = MediaItem.fromUri(it)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true

            // Renderer (SurfaceTexture via TextureView)
            val textureView = findViewById<TextureView>(R.id.texturePreview)
            videoController = MacanVideoController(this, it).apply {
                attachSurface(Surface(textureView.surfaceTexture))
                prepare()
            }

            findViewById<Button>(R.id.btnPlay).setOnClickListener {
                videoController?.play()
            }
            findViewById<Button>(R.id.btnPause).setOnClickListener {
                videoController?.pause()
            }
            findViewById<Button>(R.id.btnStop).setOnClickListener {
                videoController?.stop()
            }

            // Optional: if using VirtualSurfaceRenderer
            val renderer = VirtualSurfaceRenderer(this, it)
            val surfaceTexture = renderer.initRenderer()

            textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(st: android.graphics.SurfaceTexture, w: Int, h: Int) {
                    textureView.setSurfaceTexture(surfaceTexture)
                    renderer.startPlayback()
                }

                override fun onSurfaceTextureSizeChanged(st: android.graphics.SurfaceTexture, width: Int, height: Int) {}
                override fun onSurfaceTextureDestroyed(st: android.graphics.SurfaceTexture): Boolean = true
                override fun onSurfaceTextureUpdated(st: android.graphics.SurfaceTexture) {}
            }

            virtualRenderer?.stopPlayback()
            virtualRenderer = renderer
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        player = ExoPlayer.Builder(this).build()
        val playerView = findViewById<PlayerView>(R.id.playerView)
        playerView.player = player

        findViewById<Button>(R.id.btnPilihVideo).setOnClickListener {
            launcher.launch("video/*")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        videoController?.release()
        virtualRenderer?.stopPlayback()
    }
}
