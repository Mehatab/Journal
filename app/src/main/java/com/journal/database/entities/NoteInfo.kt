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

package com.journal.database.entities

import com.editor.util.SplitHeadingAndBody
import com.journal.common.DiffItem

data class NoteInfo(
    var note_id: Long? = null,
    var note: String? = null,
    var notebook_id: Long? = null,
    var color: String? = null
) : DiffItem {
    override fun getId() = note_id ?: 0
    override fun getContent() = toString()
    fun getTitle() = SplitHeadingAndBody.split(note ?: "").first
    fun getBody() = SplitHeadingAndBody.split(note ?: "").second
}