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