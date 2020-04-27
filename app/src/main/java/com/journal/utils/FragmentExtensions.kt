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

package com.journal.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.use
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.journal.database.entities.Note
import com.journal.database.entities.NoteInfo
import com.journal.database.entities.Notebook
import com.journal.home.HomeFragment
import com.journal.home.HomeFragmentDirections
import com.journal.notebook.details.NotebookFragment
import com.journal.notebook.details.NotebookFragmentDirections

fun AppCompatActivity.setDefaultNightMode() {
    val theme =
        PreferenceManager.getDefaultSharedPreferences(this).getString(THEME, SYSTEM) ?: SYSTEM
    AppCompatDelegate.setDefaultNightMode(theme.toInt())
}

fun NotebookFragment.editNote(note: Note, view: View) {
    val action = NotebookFragmentDirections.notebookToEditor(
        note.notebookId ?: 0,
        note.noteId ?: 0,
        view.transitionName
    )
    findNavController().navigate(action, FragmentNavigatorExtras(view to view.transitionName))
}

fun NotebookFragment.editNoteBook(notebookId: Long) {
    findNavController().navigate(NotebookFragmentDirections.actionNotebookToEdit(notebookId))
}

fun NotebookFragment.newNote(notebookId: Long, view: View) {
    val transitionName = view.transitionName
    val action = NotebookFragmentDirections.actionNotebookToEditor(
        notebookId,
        0,
        transitionName
    )
    findNavController().navigate(action, FragmentNavigatorExtras(view to transitionName))
}

fun HomeFragment.newNote(view: View) {
    val transitionName = view.transitionName
    val action = HomeFragmentDirections.homeToEditor(1, 0, view.transitionName)
    findNavController().navigate(action, FragmentNavigatorExtras(view to transitionName))
}


fun HomeFragment.editNote(note: NoteInfo, view: View) {
    val action = HomeFragmentDirections.homeToEditor(
        note.notebook_id ?: 0,
        note.note_id ?: 0,
        view.transitionName
    )
    findNavController().navigate(action, FragmentNavigatorExtras(view to view.transitionName))
}

fun HomeFragment.openNotebook(notebook: Notebook) {
    val action = HomeFragmentDirections.homeToNotebook(notebook.notebookId ?: 0)
    findNavController().navigate(action)
}



@SuppressLint("Recycle")
fun Context.themeInterpolator(@AttrRes attr: Int): Interpolator {
    return AnimationUtils.loadInterpolator(
        this,
        obtainStyledAttributes(intArrayOf(attr)).use {
            it.getResourceId(0, android.R.interpolator.fast_out_slow_in)
        }
    )
}