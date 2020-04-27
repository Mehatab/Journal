/*
 * Copyright 2020 Mehatab Shaikh.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journal.notebook.details

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.journal.database.JournalDB
import com.journal.database.entities.Note
import com.journal.database.entities.Notebook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotebookViewModel(application: Application) : AndroidViewModel(application) {


    private val noteDao by lazy {
        JournalDB.get(application).getNoteDao()
    }

    val query = MutableLiveData<String>()

    val notebookId = MutableLiveData<Long>()
    val ascOrder = MutableLiveData(false)

    val notes = Transformations.switchMap(query) { q ->
        Transformations.switchMap(notebookId) { id ->
            Transformations.switchMap(ascOrder) {
                noteDao.getNotes(id, "%$q%", it)
            }
        }
    }

    val notebook = Transformations.switchMap(notebookId) {
        noteDao.getNotebook(it)
    }


    fun getNote(position: Int): Note {
        return notes.value?.get(position) ?: Note()
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteNotebook(notebookId.value ?: 0L)
        }
    }

    init {
        query.postValue("")
    }
}