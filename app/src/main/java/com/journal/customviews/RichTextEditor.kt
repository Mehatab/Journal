package com.journal.customviews

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText

class RichTextEditor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), TextWatcher {
    private val TAG = RichTextEditor::class.java.simpleName

    init {
        addTextChangedListener(this)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        Log.d(TAG, "onTextChanged   $text $start $lengthBefore $lengthAfter")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d(TAG, "afterTextChanged   ${s.toString()}")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d(TAG, "beforeTextChanged    $s $start $count $after")
    }
}