package com.journal.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.journal.common.DiffItem

@Entity(tableName = "Notebook")
class Notebook(
    @PrimaryKey
    @ColumnInfo(name = "notebook_id")
    var notebookId: Long? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "color")
    var color: String? = null
) : DiffItem {
    override fun getId() = notebookId ?: 0
    override fun getContent() = toString()
}