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