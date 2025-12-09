package com.medioambiente.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.medioambiente.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)

        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.youtube_player_view)
        val videoProgressBar: ProgressBar = findViewById(R.id.videoProgressBar)

        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "2j6MFrLjCbA"
                youTubePlayer.loadVideo(videoId, 0f)
                youTubePlayer.play()
                youTubePlayer.unMute()
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                when (state) {
                    PlayerConstants.PlayerState.PLAYING,
                    PlayerConstants.PlayerState.PAUSED,
                    PlayerConstants.PlayerState.ENDED,
                    PlayerConstants.PlayerState.UNSTARTED -> {
                        videoProgressBar.visibility = View.GONE
                    }
                    PlayerConstants.PlayerState.BUFFERING -> {
                        videoProgressBar.visibility = View.VISIBLE
                    }
                    else -> {
                        videoProgressBar.visibility = View.GONE
                    }
                }
            }
        })

        val backButton: Button = findViewById(R.id.backToHomeButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}