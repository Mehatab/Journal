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