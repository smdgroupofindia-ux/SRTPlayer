package com.example.streamplayer

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.example.streamplayer.databinding.ActivityMainBinding

/**
 * Professional, High-Quality SRT Player.
 * Optimized for low latency and production stability.
 */
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep screen on during playback
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.playButton.setOnClickListener {
            val url = binding.urlInput.text.toString().trim()
            if (url.isNotEmpty()) {
                playSrtStream(url)
            } else {
                showToast("Please enter a valid SRT URL")
            }
        }
    }

    private fun playSrtStream(url: String) {
        releasePlayer()
        
        // Advanced LoadControl for Ultra-Low Latency
        val loadControl = DefaultLoadControl.Builder()
            .setBufferMs(500, 1000, 250, 500) // Min, Max, Playback, Rebuffer
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        player = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .setBandwidthMeter(DefaultBandwidthMeter.getSingletonInstance(this))
            .build()
            .also { exoPlayer ->
                binding.playerView.player = exoPlayer
                
                val srtUrl = if (url.startsWith("srt://", ignoreCase = true)) url else "srt://$url"
                val mediaSource = createSrtMediaSource(srtUrl)

                if (mediaSource != null) {
                    exoPlayer.setMediaSource(mediaSource)
                    exoPlayer.addListener(object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            showToast("SRT Playback Error: ${error.message}")
                        }

                        override fun onPlaybackStateChanged(state: Int) {
                            when (state) {
                                Player.STATE_BUFFERING -> binding.progressBar.visibility = View.VISIBLE
                                Player.STATE_READY -> binding.progressBar.visibility = View.GONE
                                Player.STATE_ENDED -> showToast("Stream Ended")
                                else -> {}
                            }
                        }
                    })

                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                }
            }
    }

    private fun createSrtMediaSource(url: String): ProgressiveMediaSource? {
        return try {
            val uri = Uri.parse(url)
            val host = uri.host ?: return null
            val port = if (uri.port != -1) uri.port else 9000
            val passPhrase = uri.getQueryParameter("passphrase")
            
            val srtFactory = SrtDataSourceFactory(host, port, passPhrase)
            ProgressiveMediaSource.Factory(srtFactory)
                .createMediaSource(MediaItem.fromUri(Uri.EMPTY))
        } catch (e: Exception) {
            showToast("Invalid SRT URL format")
            null
        }
    }

    private fun releasePlayer() {
        player?.let {
            it.release()
            player = null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        releasePlayer()
    }
}
