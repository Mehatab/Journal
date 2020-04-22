package com.journal.notebook.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.journal.R
import com.journal.common.GenericRVAdapter
import com.journal.database.entities.Note
import com.journal.databinding.FragmentNotebookBinding
import com.journal.editor.RichEditorFragment
import com.journal.notebook.NewNotebook
import com.journal.utils.NOTEBOOK_ID
import com.journal.utils.requireNavigation

class NotebookFragment : Fragment() {
    private lateinit var binding: FragmentNotebookBinding
    private val viewModel by viewModels<NotebookViewModel>()
    private lateinit var adapter: GenericRVAdapter<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.notebookId.postValue(it.getLong(NOTEBOOK_ID, 0L))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotebookBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
        }
        binding.viewModel = viewModel
        adapter = GenericRVAdapter(R.layout.adapter_note_item)
        binding.rv.adapter = adapter
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        viewModel.notes.observe(viewLifecycleOwner, Observer {
            adapter.items = it
        })

        binding.add.setOnClickListener {
            requireNavigation()?.pushFragment(RichEditorFragment.newInstance(Bundle().apply {
                putLong(NOTEBOOK_ID, viewModel.notebookId.value ?: 0)
            }))
        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.edit.setOnClickListener {
            requireNavigation()?.showDialogFragment(NewNotebook.newInstance(Bundle().apply {
                putLong(NOTEBOOK_ID, viewModel.notebookId.value ?: 0)
            }))
        }
    }

    companion object {
        fun newInstance(bundle: Bundle? = Bundle()) = NotebookFragment().apply {
            arguments = bundle
        }
    }
}