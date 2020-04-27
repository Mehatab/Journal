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

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.journal.R
import com.journal.common.GenericRVAdapter
import com.journal.database.entities.Note
import com.journal.databinding.FragmentNotebookBinding
import com.journal.utils.*

class NotebookFragment : Fragment(), GenericRVAdapter.OnListItemViewClickListener {
    private lateinit var binding: FragmentNotebookBinding
    private val viewModel by viewModels<NotebookViewModel>()
    private var adapter = GenericRVAdapter<Note>(R.layout.adapter_note_item, this)
    private var scrollToTop = false
    private val args: NotebookFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentNotebookBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.notebookId.postValue(args.notebookId)

        binding.notesRv.adapter = adapter

        viewModel.notes.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            if (scrollToTop) binding.notesRv.scrollToPosition(0)
            scrollToTop = false
        })

        binding.add.setOnClickListener {
            newNote(viewModel.notebookId.value ?: 0, binding.add)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener onOptionsItemSelected(it.itemId)
        }

        binding.sort.setOnClickListener { v ->
            scrollToTop = true
            v.rotate()
            viewModel.ascOrder.postValue(v.isSelected.not())
        }
    }

    override fun onClick(view: View, position: Int) {
        when (view.id) {
            R.id.note_layout -> {
                editNote(viewModel.getNote(position), view)
            }
        }
    }

    private fun delete() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_notebook, viewModel.notebooks.value?.title ?: ""))
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(android.R.string.yes) { dialog, _ ->
                viewModel.delete()
                dialog.dismiss()
                findNavController().navigateUp()
            }.show()
    }

    private fun onOptionsItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.edit -> {
                editNoteBook(viewModel.notebookId.value ?: 0)
                true
            }
            R.id.delete -> {
                delete()
                true
            }
            else -> false
        }
    }
}