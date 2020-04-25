package com.journal.utils

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.noties.markwon.Markwon

@BindingAdapter("setColor")
fun setColor(radioButton: View, color: String) {
    radioButton.background?.setTint(Color.parseColor(color))
}


@BindingAdapter("setMarkdown")
fun setMarkdown(tv: TextView, string: String? = null) {
    Markwon.create(tv.context).setMarkdown(tv, string ?: "")
}