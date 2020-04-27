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

package com.journal.editor

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.journal.database.JournalDB
import com.journal.database.entities.Note
import com.journal.utils.Coroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RichEditorViewModel(application: Application) : AndroidViewModel(application),
    LifecycleEventObserver {
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
        Coroutines.ioThenMain({
            noteDao.draft(Note(notebookId = notebookId, note = "# "))
        }, {
            noteId.postValue(it)
        })
    }

    fun update() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.update(note.get()?.apply {
                note = note?.trim()
            })
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            CoroutineScope(Dispatchers.IO).launch {
                noteDao.deleteDrafts()
            }
        }
    }
}