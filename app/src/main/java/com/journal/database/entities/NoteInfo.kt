package com.journal.database.entities

import com.journal.common.DiffItem

data class NoteInfo(
    var noteId: Long? = null,
    var note_title: String? = null,
    var note: String? = null,
    var notebookId: Long? = null,
    var color: String? = null
) : DiffItem {
    override fun getId() = noteId ?: 0
    override fun getContent() = toString()
}