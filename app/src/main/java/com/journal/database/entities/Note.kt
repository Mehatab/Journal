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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.editor.util.SplitHeadingAndBody
import com.journal.common.DiffItem

@Entity(
    tableName = "Note", foreignKeys = [ForeignKey(
        entity = Notebook::class,
        childColumns = ["notebook_id"],
        parentColumns = ["notebook_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
class Note(
    @PrimaryKey
    @ColumnInfo(name = "note_id")
    var noteId: Long? = null,
    @ColumnInfo(name = "note")
    var note: String? = null,
    @ColumnInfo(name = "notebook_id")
    var notebookId: Long? = null
) : DiffItem {
    override fun getId() = noteId ?: 0
    override fun getContent() = toString()
    fun getTitle() = SplitHeadingAndBody.split(note ?: "").first
    fun getBody() = SplitHeadingAndBody.split(note ?: "").second
    fun getNoteContent() = note ?: ""
}