package com.journal.utils

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter
import com.editor.setMarkdown

@BindingAdapter("setColor")
fun setColor(radioButton: View, color: String) {
    radioButton.background?.setTint(Color.parseColor(color))
}


@BindingAdapter("setMarkdown")
fun setMarkdown(tv: TextView, string: String? = null) {
    tv.setMarkdown(string)
}

@BindingAdapter("setSelected")
fun setSelected(tv: AppCompatImageButton, isSelected: Boolean? = false) {
    tv.isSelected = isSelected ?: false
}