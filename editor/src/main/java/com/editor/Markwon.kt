package com.editor

import android.widget.TextView
import ru.noties.markwon.Markwon


fun TextView.setMarkdown(string: String? = null) {
    Markwon.create(context).setMarkdown(this, string ?: "")
}