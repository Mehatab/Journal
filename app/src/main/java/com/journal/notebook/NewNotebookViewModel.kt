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

package com.journal.notebook

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.journal.database.JournalDB
import com.journal.database.entities.Notebook
import com.journal.database.entities.NotebookColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewNotebookViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao by lazy {
        JournalDB.get(application).getNoteDao()
    }

    var notebookId = MutableLiveData<Long>()
    val title = ObservableField("")


    val selectedColor = MutableLiveData<String>()

    val colors: LiveData<ArrayList<NotebookColor>> = Transformations.map(selectedColor) { color ->
        return@map getColors().apply {
            forEach {
                it.isSelected = it.color == selectedColor.value
            }
        }
    }

    val note = Transformations.switchMap(notebookId) { id ->
        noteDao.getNotebook(id)
    }

    init {
        selectedColor.postValue("#9E5CFD")
    }

    fun updateSelection(position: Int) {
        selectedColor.postValue(colors.value!![position].color ?: "")
    }

    private fun getColors() = arrayListOf<NotebookColor>().apply {
        add(NotebookColor(color = "#9E5CFD"))
        add(NotebookColor(color = "#FF76BD"))
        add(NotebookColor(color = "#4449E1"))
        add(NotebookColor(color = "#7378FF"))
        add(NotebookColor(color = "#73B9FF"))
        add(NotebookColor(color = "#6EE144"))
        add(NotebookColor(color = "#F6E73A"))
        add(NotebookColor(color = "#FF7070"))
        add(NotebookColor(color = "#E14444"))
    }

    fun <T> save(success: (String) -> T) {
        if (!title.get().isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                noteDao.insert(
                    Notebook(
                        notebookId = notebookId.value,
                        title = title.get(),
                        color = selectedColor.value
                    )
                )
            }
            success("")
        }
    }
}