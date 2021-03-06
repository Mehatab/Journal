package com.editor

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.style.*
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.editor.spans.HeadingSpanWithLevel
import com.editor.spans.HorizontalRuleSpan
import com.editor.spans.IndentedCodeBlockSpan
import com.editor.spans.InlineCodeSpan
import ru.noties.markwon.core.MarkwonTheme
import ru.noties.markwon.core.spans.BlockQuoteSpan
import java.util.*

/**
 * Pool for reusing spans instead of creating and throwing them on every text change.
 */
@SuppressLint("UseSparseArrays")
class MarkdownSpanPool {

    private val italicsSpans = Stack<StyleSpan>()
    private val boldSpans = Stack<StyleSpan>()
    private val strikethroughSpans = Stack<StrikethroughSpan>()
    private val monospaceTypefaceSpans = Stack<TypefaceSpan>()
    private val foregroundColorSpans = HashMap<Int, ForegroundColorSpan>()
    private val inlineCodeSpans = Stack<InlineCodeSpan>()
    private val indentedCodeSpans = Stack<IndentedCodeBlockSpan>()
    private val headingSpans = HashMap<Int, HeadingSpanWithLevel>()
    private val superscriptSpans = Stack<SuperscriptSpan>()
    private val quoteSpans = Stack<BlockQuoteSpan>()
    private val leadingMarginSpans = HashMap<Int, LeadingMarginSpan.Standard>()
    private val horizontalRuleSpans = HashMap<String, HorizontalRuleSpan>()

    fun italics(): StyleSpan {
        return when {
            italicsSpans.empty() -> StyleSpan(Typeface.ITALIC)
            else -> italicsSpans.pop()
        }
    }

    fun bold(): StyleSpan {
        return when {
            boldSpans.empty() -> StyleSpan(Typeface.BOLD)
            else -> boldSpans.pop()
        }
    }

    fun foregroundColor(@ColorInt color: Int): ForegroundColorSpan {
        return when {
            foregroundColorSpans.containsKey(color) -> foregroundColorSpans.remove(color)!!
            else -> ForegroundColorSpan(color)
        }
    }

    fun inlineCode(markwonTheme: MarkwonTheme): InlineCodeSpan {
        return when {
            inlineCodeSpans.empty() -> InlineCodeSpan(markwonTheme)
            else -> inlineCodeSpans.pop()
        }
    }

    fun indentedCodeBlock(markwonTheme: MarkwonTheme): IndentedCodeBlockSpan {
        return when {
            indentedCodeSpans.empty() -> IndentedCodeBlockSpan(markwonTheme)
            else -> indentedCodeSpans.pop()
        }
    }

    fun strikethrough(): StrikethroughSpan {
        return when {
            strikethroughSpans.empty() -> StrikethroughSpan()
            else -> strikethroughSpans.pop()
        }
    }

    fun monospaceTypeface(): TypefaceSpan {
        return when {
            monospaceTypefaceSpans.empty() -> TypefaceSpan("monospace")
            else -> monospaceTypefaceSpans.pop()
        }
    }

    fun heading(level: Int, markwonTheme: MarkwonTheme): HeadingSpanWithLevel {
        return when {
            headingSpans.containsKey(level) -> headingSpans.remove(level)!!
            else -> HeadingSpanWithLevel(markwonTheme, level)
        }
    }

    fun superscript(): SuperscriptSpan {
        return when {
            superscriptSpans.empty() -> SuperscriptSpan()
            else -> superscriptSpans.pop()
        }
    }

    fun quote(markwonTheme: MarkwonTheme): BlockQuoteSpan {
        return when {
            quoteSpans.empty() -> BlockQuoteSpan(markwonTheme)
            else -> quoteSpans.pop()
        }
    }

    fun leadingMargin(margin: Int): LeadingMarginSpan.Standard {
        return when {
            leadingMarginSpans.containsKey(margin) -> leadingMarginSpans.remove(margin)!!
            else -> LeadingMarginSpan.Standard(margin)
        }
    }

    /**
     * @param text See [HorizontalRuleSpan.HorizontalRuleSpan].
     */
    fun horizontalRule(
        text: CharSequence,
        @ColorInt ruleColor: Int,
        @Px ruleStrokeWidth: Int,
        mode: HorizontalRuleSpan.Mode
    ): HorizontalRuleSpan {
        val key = text.toString() + "_" + ruleColor + "_" + ruleStrokeWidth + "_" + mode
        return when {
            horizontalRuleSpans.containsKey(key) -> horizontalRuleSpans.remove(key)!!
            else -> HorizontalRuleSpan(text, ruleColor, ruleStrokeWidth, mode)
        }
    }

    fun recycle(span: Any) {
        when (span) {
            is StyleSpan -> recycle(span)
            is ForegroundColorSpan -> recycle(span)
            is StrikethroughSpan -> recycle(span)
            is TypefaceSpan -> recycle(span)
            is HeadingSpanWithLevel -> recycle(span)
            is SuperscriptSpan -> recycle(span)
            is BlockQuoteSpan -> recycle(span)
            is LeadingMarginSpan.Standard -> recycle(span)
            is HorizontalRuleSpan -> recycle(span)
            is IndentedCodeBlockSpan -> recycle(span)
            is InlineCodeSpan -> recycle(span)
            else -> throw UnsupportedOperationException("Unknown span: " + span.javaClass.simpleName)
        }
    }

    fun recycle(span: StyleSpan) {
        when {
            span.style == Typeface.ITALIC -> italicsSpans.push(span)
            span.style == Typeface.BOLD -> boldSpans.add(span)
            else -> throw UnsupportedOperationException("Only italics and bold spans supported.")
        }
    }

    fun recycle(span: ForegroundColorSpan) {
        val key = span.foregroundColor
        foregroundColorSpans[key] = span
    }

    fun recycle(span: InlineCodeSpan) {
        inlineCodeSpans.push(span)
    }

    fun recycle(span: IndentedCodeBlockSpan) {
        indentedCodeSpans.push(span)
    }

    fun recycle(span: StrikethroughSpan) {
        strikethroughSpans.push(span)
    }

    fun recycle(span: TypefaceSpan) {
        if (span.family != "monospace") {
            throw UnsupportedOperationException("Only monospace typeface spans exist in this pool.")
        }
        monospaceTypefaceSpans.push(span)
    }

    fun recycle(span: HeadingSpanWithLevel) {
        headingSpans[span.level] = span
    }

    fun recycle(span: SuperscriptSpan) {
        superscriptSpans.push(span)
    }

    fun recycle(span: BlockQuoteSpan) {
        quoteSpans.push(span)
    }

    fun recycle(span: LeadingMarginSpan.Standard) {
        val key = span.getLeadingMargin(true /* irrelevant */)
        leadingMarginSpans[key] = span
    }

    fun recycle(span: HorizontalRuleSpan) {
        val key =
            span.text.toString() + "_" + span.ruleColor + "_" + span.ruleStrokeWidth + "_" + span.mode
        horizontalRuleSpans[key] = span
    }
}
