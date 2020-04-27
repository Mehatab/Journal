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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.journal.R
import com.journal.common.GenericRVAdapter
import com.journal.database.entities.NotebookColor
import com.journal.databinding.FragmentNewNotebookBinding

class NewNotebook : BottomSheetDialogFragment(), GenericRVAdapter.OnListItemViewClickListener {
    private lateinit var binding: FragmentNewNotebookBinding
    private var adapter = GenericRVAdapter<NotebookColor>(R.layout.adapter_color_item, this)
    private val mViewModel by viewModels<NewNotebookViewModel>()
    private val args: NewNotebookArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewNotebookBinding.inflate(inflater).apply {
            viewModel = mViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.notebookId.postValue(if (args.notebookId == -1L) null else args.notebookId)

        binding.rv.adapter = adapter

        mViewModel.colors.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })

        mViewModel.note.observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewModel.title.set(it.title)
                mViewModel.selectedColor.postValue(it.color)
            }
        })

        binding.title.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mViewModel.save {
                    dismiss()
                }
            }
            return@setOnEditorActionListener true
        }
    }

    override fun onClick(view: View, position: Int) {
        mViewModel.updateSelection(position)
        adapter.notifyDataSetChanged()
    }
}