package com.editor

import android.text.Spannable

/**
 * See [FlexmarkMarkdownParser].
 */
interface MarkdownParser {

    fun parseSpans(text: Spannable): MarkdownHintsSpanWriter

    /**
     * Called on every text change so that stale spans can
     * be removed before applying new ones.
     */
    fun removeSpans(text: Spannable)
}
