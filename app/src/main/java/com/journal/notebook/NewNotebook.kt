package com.journal.notebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.journal.R
import com.journal.common.GenericRVAdapter
import com.journal.database.entities.NotebookColor
import com.journal.databinding.FragmentNewNotebookBinding
import com.journal.utils.NOTEBOOK_ID

class NewNotebook : BottomSheetDialogFragment(), GenericRVAdapter.OnListItemViewClickListener {
    private lateinit var binding: FragmentNewNotebookBinding
    private lateinit var adapter: GenericRVAdapter<NotebookColor>
    private val viewModel by viewModels<NewNotebookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewNotebookBinding.inflate(inflater)
        binding.viewModel = viewModel

        adapter = GenericRVAdapter(R.layout.adapter_color_item, this)
        binding.rv.adapter = adapter

        viewModel.colors.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })

        viewModel.note.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.title.set(it.title)
                viewModel.selectedColor.postValue(it.color)
            }
        })

        binding.title.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.save {
                    dismiss()
                }
            }
            return@setOnEditorActionListener true
        }

        return binding.root
    }

    override fun onClick(view: View, position: Int) {
        viewModel.updateSelection(position)
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.notebookId.postValue(it.getLong(NOTEBOOK_ID))
        }
    }
}