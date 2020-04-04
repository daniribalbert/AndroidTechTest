package com.buffup.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.buffup.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.buff_sender_layout.view.*

internal class BuffSenderView : FrameLayout {

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
        View.inflate(context, R.layout.buff_sender_layout, this)
    }

    private lateinit var name: String
    private var image: String? = null

    fun setSender(name: String, image: String? = null) {
        this.name = name
        this.image = image
        updateLayout()
    }

    private fun updateLayout() {
        tvSenderName.text = name
        Glide.with(context)
            .load(image)
            .apply(RequestOptions.circleCropTransform())
            .into(ivSenderImage)
    }
}