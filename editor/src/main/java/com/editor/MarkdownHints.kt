package com.editor

import android.widget.EditText
import com.editor.util.AfterTextChange
import com.editor.util.OnViewDetach
import com.editor.util.UiThreadExecutor
import com.editor.util.suspendTextWatcherAndRun
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Highlights markdown syntax in real-time.
 *
 * Usage:
 * val markdownHints = MarkdownHints(EditText, MarkdownParser))
 * editText.addTextChangedListener(markdownHints.textWatcher())
 */
class MarkdownHints(
    private val editText: EditText,
    private val parser: MarkdownParser
) {

    private val bgExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val uiExecutor = UiThreadExecutor()

    init {
        cleanupOnViewDetach()
    }

    private fun cleanupOnViewDetach() {
        editText.addOnAttachStateChangeListener(OnViewDetach {
            bgExecutor.shutdownNow()
        })
    }

    fun textWatcher() = AfterTextChange { editable, textWatcher ->
        val textLengthToParse = editable.length

        bgExecutor.submit {
            val spanWriter = parser.parseSpans(editable)

            uiExecutor.execute {
                // Because the text is parsed in background, it is possible
                // that the text changes faster than they get processed.
                val isStale = textLengthToParse != editText.text.length

                if (isStale.not()) {
                    editText.suspendTextWatcherAndRun(textWatcher) {
                        parser.removeSpans(editable)
                        spanWriter.writeTo(editable)
                    }
                }
            }
        }.get()
    }
}
