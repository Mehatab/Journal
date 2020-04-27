package com.journal.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.journal.database.JournalDB
import com.journal.database.entities.NoteInfo

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao by lazy {
        JournalDB.get(application).getNoteDao()
    }

    val query = MutableLiveData<String>("")

    val notebooks = Transformations.switchMap(query) { noteDao.getNotebooks("%$it%") }

    val notes = Transformations.switchMap(query) { noteDao.getNotes("%$it%") }

    val fabVisibility = noteDao.hasNotebooks()

    fun getNotebook(position: Int) = notebooks.value?.get(position)

    fun getNote(position: Int): NoteInfo {
        return notes.value?.get(position) ?: NoteInfo()
    }
}