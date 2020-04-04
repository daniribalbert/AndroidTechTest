package com.daniribalbert.videoapp.ui.videoplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideoPlayerViewModel : ViewModel() {

    companion object {
        private const val VIDEO_URL =
            "https://buffup-public.s3.eu-west-2.amazonaws.com/video/toronto+nba+cut+3.mp4"
    }

    val videoPlayerLiveData by lazy { MutableLiveData<String>() }

    /**
     * Currently using Live
     */
    fun loadVideoUrl() {
        /**
         * Currently video URL is hardcoded, but in the future or other apps might want to fetch this URL.
         */
        videoPlayerLiveData.postValue(VIDEO_URL)
    }
}