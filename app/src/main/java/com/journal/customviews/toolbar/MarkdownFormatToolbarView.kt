/*
 * Copyright 2020 Mehatab Shaikh.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journal.customviews.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import com.journal.R

class MarkdownFormatToolbarView(context: Context, attrs: AttributeSet) :
    HorizontalScrollView(context, attrs) {

    lateinit var onInsertLinkClicked: () -> Unit
    lateinit var onMarkdownSyntaxApplied: (MarkdownSyntax) -> Unit

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_text_formatting_toolbar, this, true)

        val insertLinkButton = findViewById<View>(R.id.textformattoolbar_insert_link)
        insertLinkButton.setOnClickListener { onInsertLinkClicked() }

        val markdownSyntaxActions = mapOf(
            R.id.textformattoolbar_bold to Bold,
            R.id.textformattoolbar_italic to Italic,
            R.id.textformattoolbar_strikethrough to StrikeThrough,
            R.id.textformattoolbar_quote to Quote,
            R.id.textformattoolbar_inline_code to InlineCode,
            R.id.textformattoolbar_header to Heading,
            R.id.textformattoolbar_thematic_break to ThematicBreak
        )

        markdownSyntaxActions.entries.forEach { (buttonId, syntax) ->
            findViewById<View>(buttonId).setOnClickListener {
                onMarkdownSyntaxApplied(syntax)
            }
        }
    }
}
