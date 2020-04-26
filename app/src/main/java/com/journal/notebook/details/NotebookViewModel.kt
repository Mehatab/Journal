package com.journal.notebook.details

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.journal.database.JournalDB
import com.journal.database.entities.Note
import com.journal.database.entities.Notebook

class NotebookViewModel(application: Application) : AndroidViewModel(application) {


    private val noteDao by lazy {
        JournalDB.get(application).getNoteDao()
    }

    val query = MutableLiveData<String>()

    val notebook = ObservableField<Notebook>()
    val notebookId = MutableLiveData<Long>()
    val ascOrder = MutableLiveData(false)

    val notes = Transformations.switchMap(query) { q ->
        Transformations.switchMap(notebookId) { id ->
            Transformations.switchMap(ascOrder) {
                noteDao.getNotes(id, "%$q%", it)
            }
        }
    }

    val notebooks = Transformations.switchMap(notebookId) {
        noteDao.getNotebook(it)
    }


    fun getNote(position: Int): Note {
        return notes.value?.get(position) ?: Note()
    }

    init {
        query.postValue("")
    }
}