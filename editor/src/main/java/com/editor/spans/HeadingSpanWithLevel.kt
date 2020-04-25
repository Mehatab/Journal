package com.editor.spans

import ru.noties.markwon.core.MarkwonTheme
import ru.noties.markwon.core.spans.HeadingSpan

class HeadingSpanWithLevel(theme: MarkwonTheme, val level: Int) : HeadingSpan(theme, level)
