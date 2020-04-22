package com.journal.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.journal.common.DiffItem

@Entity(tableName = "Note")
class Note(
    @PrimaryKey
    @ColumnInfo(name = "note_id")
    var noteId: Long? = null,
    @ColumnInfo(name = "note_title")
    var note_title: String? = null,
    @ColumnInfo(name = "note")
    var note: String? = null,
    @ColumnInfo(name = "notebook_id")
    var notebookId: Long? = null
) : DiffItem {
    override fun getId() = noteId ?: 0
    override fun getContent() = toString()
}