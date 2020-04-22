package com.journal.utils

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.devs.vectorchildfinder.VectorChildFinder
import com.journal.R

@BindingAdapter("setColor")
fun setColor(radioButton: View, color: String) {
    radioButton.background?.setTint(Color.parseColor(color))
}


@BindingAdapter("setNotebookTint")
fun setNotebookTint(iv: AppCompatImageView, color: String? = null) {
    color?.let {
        VectorChildFinder(iv.context, R.drawable.ic_notebook, iv).apply {
            findPathByName("background").fillColor = Color.parseColor(it)
        }
        iv.invalidate()
    }
}