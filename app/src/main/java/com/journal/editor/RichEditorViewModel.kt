package com.journal.editor

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.journal.database.JournalDB
import com.journal.database.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RichEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao by lazy {
        JournalDB.get(application).getNoteDao()
    }

    var notebookId = 1L
    var noteId = MutableLiveData<Long>()

    var note = ObservableField<Note>()

    val noteLiveData: LiveData<Note> = Transformations.map(noteId) { id ->
        return@map if (id > 0L) {
            viewModelScope.launch(Dispatchers.IO) {
                note.set(noteDao.getNote(id))
            }
            null
        } else {
            draft()
            null
        }
    }

    private fun draft() {
        viewModelScope.launch(Dispatchers.IO) {
            noteId.postValue(noteDao.insert(Note(notebookId = notebookId, note = "# ")))
        }
    }

    fun update() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.update(note.get())
        }
    }

}