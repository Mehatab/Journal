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