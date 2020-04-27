package com.journal.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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