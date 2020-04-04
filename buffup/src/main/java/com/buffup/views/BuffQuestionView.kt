package com.buffup.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.buffup.R
import com.buffup.api.model.Question
import kotlinx.android.synthetic.main.buff_question_layout.view.*

internal class BuffQuestionView : FrameLayout {

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
        View.inflate(context, R.layout.buff_question_layout, this)
    }

    private var time: Long = 0L
    private lateinit var question: Question

    fun setQuestion(question: Question, time: Long) {
        this.question = question
        this.time = time
        updateLayout()
    }

    private fun updateLayout() {
        tvQuestion.text = question.title
        buffTimer.setCountdownMax(time.toInt())
        buffTimer.start()
    }

    fun setListener(listener: TimerView.Listener) {
        buffTimer.listener = listener
    }

    fun stopTimer() {
        buffTimer.stop()
    }
}