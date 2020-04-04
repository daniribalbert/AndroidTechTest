package com.buffup.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buffup.R
import com.buffup.api.model.Answer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.buff_answer_layout.view.*

internal class BuffAnswersAdapter(val clickCallback: (Answer) -> Unit) : RecyclerView.Adapter<BuffAnswersAdapter.AnswerViewHolder>() {

    var answerList = emptyList<Answer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.buff_answer_layout, parent, false)
        return AnswerViewHolder(view)
    }

    override fun getItemCount(): Int = answerList.size

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(answerList[position])
    }

    inner class AnswerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener { clickCallback.invoke(answerList[adapterPosition]) }
        }

        fun bind(answer: Answer) {
            val ctx = view.context
            view.tvAnswer.text = answer.title
            Glide.with(ctx).load(answer.getImageUrl(ctx)).centerInside().into(view.ivAnswerIcon)
        }
    }
}