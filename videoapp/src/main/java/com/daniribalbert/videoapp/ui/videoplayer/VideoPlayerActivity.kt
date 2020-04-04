package com.daniribalbert.videoapp

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.buffup.api.utils.BuffError
import com.buffup.model.BuffEvent
import com.buffup.views.BuffView
import com.daniribalbert.videoapp.ui.videoplayer.VideoPlayerViewModel
import kotlinx.android.synthetic.main.video_player_activity.*
import timber.log.Timber

class VideoPlayerActivity : AppCompatActivity(), BuffView.Listener {

    lateinit var videoPlayerViewModel: VideoPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_player_activity)

        videoPlayerViewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)

        setupListeners()
        setupObservers()
    }

    override fun onStart() {
        super.onStart()
        videoPlayerViewModel.loadVideoUrl()
    }

    private fun setupListeners() {
        buffView.listener = this
    }

    private fun setupObservers() {
        videoPlayerViewModel.videoPlayerLiveData.observe(this, Observer { url ->
            Timber.d("Playing video url... $url")
            play(url)
        })
    }

    private fun play(url: String) {
        videoPlayerView.setVideoURI(Uri.parse(url))
        videoPlayerView.setOnPreparedListener {
            videoPlayerView.start()
            buffView.start()
        }
    }

    override fun onStop() {
        super.onStop()
        if (videoPlayerView.isPlaying) {
            videoPlayerView.stopPlayback()
        }
        buffView.stop()
    }

    override fun onErrorLoadingBuff(error: BuffError) {
        when (error) {
            is BuffError.HttpError -> Timber.w("Client received Buff HTTP error")
            is BuffError.GenericError -> Timber.w("Client received Buff GenericError error")
        }
    }

    override fun onBuffEvent(event: BuffEvent) {
        when (event) {
            BuffEvent.ON_TIMER_EXPIRED -> Timber.d("ON_TIMER_EXPIRED")
            BuffEvent.ON_ANSWER_SELECTED -> Timber.d("ON_ANSWER_SELECTED")
            BuffEvent.ON_USER_CLOSED_QUESTION -> Timber.d("ON_USER_CLOSED_QUESTION")
            BuffEvent.ON_QUESTION_LOADED -> Timber.d("ON_QUESTION_LOADED")
        }
    }
}