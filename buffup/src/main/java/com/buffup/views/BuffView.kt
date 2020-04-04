package com.buffup.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.buffup.R
import com.buffup.api.BuffRepository
import com.buffup.api.model.Answer
import com.buffup.api.model.BuffResponse
import com.buffup.api.utils.BuffError
import com.buffup.api.utils.BaseResult
import com.buffup.model.BuffEvent
import kotlinx.android.synthetic.main.buff_layout.view.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

open class BuffView : FrameLayout, TimerView.Listener, BuffViewPresenter.Listener {

    /**
     * Buff Listener. Set this to receive useful events in your application.
     */
    var listener: Listener? = null

    /**
     * Runnable that keeps loading buffs.
     */
    private val loadBuffRunnable by lazy { Runnable { startLoadingBuffs() } }

    /**
     * Runnable to start close animation.
     */
    private val closeAnimationRunnable by lazy { Runnable { startCloseAnimation() } }

    internal open val presenter by lazy { BuffViewPresenter(BuffRepository(), this) }

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
        View.inflate(context, R.layout.buff_layout, this)
        btCloseBuff.setOnClickListener {
            startCloseAnimation()
            listener?.onBuffEvent(BuffEvent.ON_USER_CLOSED_QUESTION)
        }
        questionLayoutContainer.setListener(this)
    }

    private fun setup(buff: BuffResponse) {
        listener?.onBuffEvent(BuffEvent.ON_QUESTION_LOADED)
        senderLayoutContainer.setSender(
            "${buff.author.firstName} ${buff.author.lastName}",
            buff.author.image
        )
        questionLayoutContainer.setQuestion(buff.question, buff.timeToShow.toLong())
        answersList.adapter =
            BuffAnswersAdapter(::onAnswerSelected).apply { answerList = buff.answers }
        startEnterAnimation()
    }

    /**
     * Start() will start loading Buffs. First buff is loaded as soon as start is called and a next
     * Buff is loaded after 30 seconds.
     */
    fun start() {
        Timber.d("Buff is starting...")

        startLoadingBuffs()
    }

    /**
     * Hides Buffs view and stops future Buffs from loading.
     * Remember to call this on on your Fragment/Activity OnStop/OnPause.
     */
    fun stop() {
        Timber.d("Buff is stopping...")
        buffMainContent.visibility = View.GONE
        questionLayoutContainer.stopTimer()
        removeCallbacks(loadBuffRunnable)
        removeCallbacks(closeAnimationRunnable)
    }

    /**
     * Loads a single buff.
     */
    private fun loadBuff() {
        presenter.loadBuff((1..5L).random())
    }

    /**
     * Function that will load a buff and enqueue a request to load the next Buff.
     */
    private fun startLoadingBuffs() {
        loadBuff() // Loads Buff.
        loadNextBuff() // Enqueues next Buff request.
    }

    /**
     * On Buff answer selected.
     */
    private fun onAnswerSelected(answer: Answer) {
        Timber.d("Selected Answer: $answer")
        // Probably post answer to backend.
        //TODO: Add select answer animation.
        questionLayoutContainer.stopTimer()
        postDelayed(closeAnimationRunnable, TimeUnit.SECONDS.toMillis(2))
        listener?.onBuffEvent(BuffEvent.ON_ANSWER_SELECTED)
    }

    /**
     * Receives callback when Buff timer finishes.
     */
    override fun onTimerEnded() {
        listener?.onBuffEvent(BuffEvent.ON_TIMER_EXPIRED)
        startCloseAnimation()
    }

    /**
     * Start animation to show Buff and makes it visible.
     */
    private fun startEnterAnimation() {
        buffMainContent.visibility = View.VISIBLE
        buffMainContent.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.slide_from_left
            )
        )
    }

    /**
     * Starts the close Buff animation and hides it when finished.
     */
    private fun startCloseAnimation() {
        AnimationUtils.loadAnimation(context, R.anim.slide_to_left).apply {
            this.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(anim: Animation?) {}

                override fun onAnimationEnd(anim: Animation?) {
                    buffMainContent.visibility = View.GONE
                }

                override fun onAnimationStart(anim: Animation?) {}

            })
        }.also { buffMainContent.startAnimation(it) }
    }


    /**
     * Loads next buff after 30 seconds.
     */
    private fun loadNextBuff() {
        postDelayed(loadBuffRunnable, TimeUnit.SECONDS.toMillis(30))
    }


    override fun onDetachedFromWindow() {
        // Stop Buff loading to avoid possible memory leak when view is detached/no longer used.
        // Also important in case client app forgets to call stop().
        try {
            stop()
        } catch (e: Exception) { /* Do Nothing */
        }
        super.onDetachedFromWindow()
    }

    override fun onBuffLoaded(data: BaseResult<BuffResponse>) {
        data.result?.let { buff -> post { setup(buff) } }
        data.error?.let { error ->
            post {
                listener?.onErrorLoadingBuff(error)
            }
        }
    }

    /**
     * Buff Listener.
     * Implement this and use buffView.setListener(listener) (BuffView.listener = listener) to receive useful events in client app.
     */
    interface Listener {
        /**
         * Listens Buff loading error.
         */
        fun onErrorLoadingBuff(error: BuffError)

        /**
         * Receives miscellaneous events from Buff.
         */
        fun onBuffEvent(event: BuffEvent)
    }

}