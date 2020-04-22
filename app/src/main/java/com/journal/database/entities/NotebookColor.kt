package com.journal.database.entities

import com.journal.common.DiffItem

data class NotebookColor(
    var color: String? = null,
    var isSelected: Boolean? = false
) : DiffItem {
    override fun getId() = 0L
    override fun getContent() = toString()
}