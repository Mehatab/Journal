package com.journal.home

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
import com.journal.database.entities.NoteInfo
import com.journal.database.entities.Notebook
import com.journal.databinding.FragmentHomeBinding
import com.journal.utils.NOTEBOOK_ID

class HomeFragment : Fragment(), GenericRVAdapter.OnListItemViewClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var notebookAdapter: GenericRVAdapter<Notebook>
    private lateinit var notesAdapter: GenericRVAdapter<NoteInfo>
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        notebookAdapter = GenericRVAdapter(R.layout.adapter_journal_item, this)
        notesAdapter = GenericRVAdapter(R.layout.adapter_home_note_item, this)
        binding.booksRv.adapter = notebookAdapter
        binding.rv.adapter = notesAdapter

        viewModel.notebooks.observe(viewLifecycleOwner, Observer {
            notebookAdapter.setList(it)
        })

        viewModel.notes.observe(viewLifecycleOwner, Observer {
            notesAdapter.setList(it)
        })

        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener onOptionsItemSelected(it.itemId)
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.home_to_editor, Bundle().apply {
                putLong(NOTEBOOK_ID, 1)
            })
        }

        binding.addNotebook.setOnClickListener {
            findNavController().navigate(R.id.home_to_new_notebook)
        }
    }

    override fun onClick(view: View, position: Int) {
        when (view.id) {
            R.id.notebook -> {
                viewModel.getNotebook(position)?.let {
                    findNavController().navigate(R.id.home_to_notebook, Bundle().apply {
                        putLong(NOTEBOOK_ID, it.notebookId ?: 0)
                    })
                }
            }
        }
    }

    private fun onOptionsItemSelected(itemId: Int): Boolean {
        when (itemId) {
            R.id.setting -> findNavController().navigate(R.id.goto_setting)
        }
        return true
    }
}