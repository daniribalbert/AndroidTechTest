package com.buffup.views

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.buffup.R
import kotlinx.android.synthetic.main.timer_layout.view.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.floor

internal class TimerView : FrameLayout {

    var current = 0
        private set

    var listener: Listener? = null

    private val countDownTimer by lazy {
        object : CountDownTimer((progress.max.toLong() + 1), 250) {

            override fun onFinish() {
                Timber.d("Timer: onFinish")

                listener?.onTimerEnded()
            }

            override fun onTick(currentTimeMs: Long) {
                Timber.v("Timer: onTick $currentTimeMs")
                val currentSeconds = floor(currentTimeMs / 1000.0).toInt()
                current = currentSeconds
                tvCurrentTime.text = "$current"
                progress.progress = progress.max - currentTimeMs.toInt()
            }
        }
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.timer_layout, this)
    }

    fun start() {
        countDownTimer.cancel()
        countDownTimer.start()
    }

    fun setCountdownMax(max: Int) {
        progress.max = max * 1000 // Progress in ms
        progress.progress = 0
        tvCurrentTime.text = "$max"
    }

    fun stop() {
        countDownTimer.cancel()
    }

    interface Listener {
        fun onTimerEnded()
    }

}