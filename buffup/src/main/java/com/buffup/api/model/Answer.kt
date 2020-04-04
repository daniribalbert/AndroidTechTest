package com.buffup.api.model

import android.content.Context
import android.util.DisplayMetrics

data class Answer(val id: Long, val buffId: Long, val title: String, val image: Map<String, AnswerImage>) {

    fun getImageUrl(context: Context) :String? {
        val densityDpi = context.resources.displayMetrics.densityDpi

        return when {
            densityDpi >= DisplayMetrics.DENSITY_XXHIGH -> image["2"]?.url
            densityDpi <= DisplayMetrics.DENSITY_MEDIUM -> image["0"]?.url
            else -> image["1"]?.url
        }
    }
}