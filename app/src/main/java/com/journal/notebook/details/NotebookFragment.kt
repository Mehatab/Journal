package com.journal.notebook.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.journal.R
import com.journal.common.GenericRVAdapter
import com.journal.database.entities.Note
import com.journal.databinding.FragmentNotebookBinding
import com.journal.utils.NOTEBOOK_ID

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
            findNavController().navigate(R.id.action_notebook_to_editor, Bundle().apply {
                putLong(NOTEBOOK_ID, viewModel.notebookId.value ?: 0)
            })
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener onOptionsItemSelected(it.itemId)
        }
    }


    private fun onOptionsItemSelected(itemId: Int): Boolean {
        when (itemId) {
            R.id.edit -> {
                findNavController().navigate(R.id.action_notebook_to_edit, Bundle().apply {
                    putLong(NOTEBOOK_ID, viewModel.notebookId.value ?: 0)
                })
            }
        }
        return true
    }
}