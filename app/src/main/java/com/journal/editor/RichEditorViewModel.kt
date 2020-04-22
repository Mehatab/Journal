package com.journal.editor

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.irshulx.models.EditorTextStyle
import com.journal.database.JournalDB
import com.journal.database.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RichEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao by lazy {
        JournalDB.get(application).getNoteDao()
    }

    var notebookId = 1L
    var text: String? = null
    var title = ObservableField("")
    val style = MutableLiveData<EditorTextStyle>()

    fun updateStyle(editorTextStyle: EditorTextStyle) {
        style.postValue(editorTextStyle)
    }

    fun save(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insert(Note(note_title = title.get(), note = text, notebookId = notebookId))
        }
    }
}